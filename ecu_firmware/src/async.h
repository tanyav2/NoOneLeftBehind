#pragma once

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

#include "global_car.h"
#include "obstacle.h"


/* SPI is in Mode 0
  => SPCR
  SPIE SPE DORD MSTR CPOL CPHA SPR1 SPR0
  1    0   0    0    0    0    X    X
*/

union SPITransactionData {
    uint8_t raw_data[6]; // Raw Length of Data Set
    struct {
        uint8_t start_bit;
        union {
            int32_t raw_value;
            struct {
                int16_t speed;
                int16_t heading;
            };
        } payload;
        uint8_t end_bit;
    };
};


volatile uint8_t spi_pointer = 0;
volatile uint8_t spi_in_transaction = 0;
volatile uint8_t adas_enable = 1;
volatile SPITransactionData spi_data;

void spi_init() {
    DDRB &= ~(_BV(PB2) | _BV(PB3) | _BV(PB5)); // SS in SPI Slave
    DDRB |= _BV(PB4); // MISO becomes output
    SPCR  = _BV(SPIE) | _BV(SPE);
}

void adc_init() {
    ADMUX = 0b01000101; // Select ADC5, Ref at AVCC, left adj Data
    ADCSRA |= _BV(ADEN) | _BV(ADPS2) | _BV(ADPS1) | _BV(ADSC);
}

void int_init() {
    // External Interrupts
    DDRD &= ~(_BV(PD2) | _BV(PD3)); // INT0 and INT1 Input
    PORTD &= ~(_BV(PD2) | _BV(PD3)); // Clear pullups
    EICRA |= _BV(ISC11) | _BV(ISC01); // Trig on falling edge
    EIMSK |= _BV(INT1) | _BV(INT0);
    #ifdef DEBUG
        DDRD |= _BV(PD7);
    #endif
    obstacle_init();
    // Timer Interrupts
    TCCR1B |= _BV(CS11) | _BV(CS10); // Prescale /64, trig every 0.5s
    TIMSK1 |= _BV(TOIE1); // Enable Timer 1 Overflow Interrpt at every 0.5s
}

/* SPI Instructions
  Instruction       MSB LSB
  ====================================
  set_speed         01  <speed, int6>
  turn              10  <heading, int6>
  get_status        00  000001 [0x01]
  get_brightness    00  100001 [0x21]
  get_obstacles     00  110010 [0x32]
  get_steps         00  110011 [0x33]
  get_obstacle_nc   00  110000 [0x30]
  get_steps_nc      00  110001 [0x31]
  enable_adas       00  010001 [0x11]
  disable_adas      00  010010 [0x12]
  obstacle_override 00  010011 [0x13]
  bypass_1          00  011000 [0x18]
  bypass_2          00  011010 [0x1A]
  enable_1          00  011001 [0x19]
  enable_2          00  011011 [0x1B]

  Start bit: 0xCE
  End bit: 0x0A or 0x1A (Obstacle)
  int6 includes one sign bit and a uint5
*/

void enable_transaction(uint8_t special_code=0) {
    spi_data.start_bit = 0xCE;
    spi_data.end_bit = 0x0A | (special_code << 4);
    spi_pointer = 0;
    spi_in_transaction = 1;
}

void parse_spi_instr(uint8_t instr) {
    if (instr & 0x40) { // Set Speed
        int8_t targ_speed = instr & 0x1F;
        if (instr & 0x20) targ_speed = ~targ_speed + 1; //Sign Inversion
        car.set_speed(targ_speed);
        spi_data.payload.speed = car.get_speed();
        spi_data.payload.heading = car.get_heading();
        enable_transaction(0);
    } else if (instr & 0x80) { //Turn
        int8_t targ_heading = instr & 0x1F;
        if (instr & 0x20) targ_heading = ~targ_heading + 1;
        car.direct_turn(targ_heading);
        spi_data.payload.speed = car.get_speed();
        spi_data.payload.heading = car.get_heading();
        enable_transaction(0);
    } else switch (instr) {
        case 0x01: //get status
            spi_data.payload.speed = car.get_speed();
            spi_data.payload.heading = car.get_heading();
            enable_transaction(0);
            break;
        case 0x21: // get brightness
            spi_data.payload.raw_value = (uint16_t(ADCH) << 8) | uint16_t(ADCL);
            enable_transaction(0);
            break;
        case 0x30: // get obstacles without clear
            spi_data.payload.raw_value = car.obstacle_flag;
            enable_transaction(0);
        case 0x31: //get steps without clear
            // TODO: Aggregate and average left & Right
            spi_data.payload.speed = car.lsteps;
            enable_transaction(0);
            break;
        case 0x32: // get obstacles
            spi_data.payload.raw_value = car.obstacle_flag;
            car.obstacle_flag = 0; // Clear the flag for next check
            enable_transaction(0);
            break;
        case 0x33: // get steps
            // TODO: Aggregate and average left & Right
            spi_data.payload.speed = car.lsteps;
            enable_transaction(0);
            car.lsteps = 0;
            car.rsteps = 0;
            break;
        case 0x11: // enable_ADAS
            adas_enable = 1;
            enable_transaction(0);
            break;
        case 0x12: // disable_ADAS
            adas_enable = 0;
            enable_transaction(0);
            break;
        case 0x13: // Manual Obstacle Override
            car.obstacle_flag = 1;
            car.restore(1,1);
            enable_transaction(0);
            break;
        case 0x18: // bypass_1
            SENS_EN_MASK &= ~(1 << 4);
            enable_transaction(0);
            break;
        case 0x1A: // bypass_2
            SENS_EN_MASK &= ~(1 << 3);
            enable_transaction(0);
            break;
        case 0x19: // enable_1
            SENS_EN_MASK |= 1 << 4;
            enable_transaction(0);
            break;
        case 0x1B: // enable_2
            SENS_EN_MASK |= 1 << 3;
            enable_transaction(0);
            break;
    }
}

ISR(SPI_STC_vect) {
    cli();
    if (spi_in_transaction) {
        if (spi_pointer >= sizeof(spi_data)) { // Transmission Complete
            spi_in_transaction = 0;
        } else {
            SPDR = spi_data.raw_data[spi_pointer];
            spi_pointer ++;
        }
    } else {
        if (SPDR) parse_spi_instr(SPDR);
    }
    sei();
}

ISR(INT0_vect) {
    cli();
    if (car.speed >= 0) car.lsteps ++;
        else car.lsteps --;
    if (car.turning == 1 && car.lsteps >= car.turn_thresh) car.restore(1,1);
    /*
    if (car.rewinding) {
        if (car.lstep <= car.turn_thresh) {
            car.restore(1,1);
            car.rewinding = 0;
        }
    }
    */
    #ifdef DEBUG
        DDRD ^= _BV(PD7);
    #endif
    sei();
}

ISR(INT1_vect) {
    cli();
    if (car.speed >= 0) car.rsteps ++;
        else car.rsteps --;
    if (car.turning == -1 && car.rsteps >= car.turn_thresh) car.restore(1,1);
    #ifdef DEBUG
        DDRD ^= _BV(PD7);
    #endif
    sei();
}

ISR(TIMER1_OVF_vect) {
    if (adas_enable) {
        obstacle_avoid();
    }
}
