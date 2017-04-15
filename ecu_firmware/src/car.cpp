#include "car.h"

#define DEBUG 1
#define BAUD 9600

#include <util/setbaud.h>

/*
  Controller Assignment
  - Left: PB6, OC0B
  - Right: PB7, OC0A
  Logical Matrix
        PB6 OC0B PB7 OC1A
  FD    1   -    1   -
  RW    0   +    0   +
  LH    1   -    0   +
  RH    0   +    1   -
*/

Car::Car() {
    // GPIO Initialization
    DDRB |= _BV(PB6) | _BV(PB7);
    DDRD |= _BV(PD5) | _BV(PD6);
    // Start Timer 0 as Fast PWM, sans-Prescaler
    TCCR0A |= _BV(WGM00) | _BV(WGM01);
    TCCR0B |= _BV(CS00);
    // Non-inverting
    TCCR0A |= _BV(COM0A1) | _BV(COM0B1);
    this -> refresh_states();
    #ifdef DEBUG
    // Initialize debugger
    UBRR0H = UBRRH_VALUE;
    UBRR0L = UBRRL_VALUE;
    #if USE_2X
    UCSR0A |= _BV(U2X0);
    #else
    UCSR0A &= ~(_BV(U2X0));
    #endif
    UCSR0C = _BV(UCSZ01) | _BV(UCSZ00);
    UCSR0B = _BV(TXEN0); // Tx only
    #endif
}

void Car::refresh_states() {
    int raw_l, raw_r;
    raw_l = (this -> speed) * 32;
    raw_r = raw_l;
    // Calculate heading: negative = left
    raw_l -= 64 * (this -> heading);
    raw_r += 64 * (this -> heading);
    raw_l = (raw_l < -255) ? -255 : raw_l;
    raw_l = (raw_l > 255) ? 255 : raw_l;
    raw_r = (raw_r < -255) ? -255 : raw_r;
    raw_r = (raw_r > 255) ? 255 : raw_r;
    if (raw_l > 0) {
        PORTB |= _BV(PB6);
        OCR0B = (uint8_t) (255 - raw_l);
    } else {
        PORTB &= ~(_BV(PB6));
        OCR0B = (uint8_t) (0 - raw_l);
    }
    if (raw_r > 0) {
        PORTB |= _BV(PB7);
        OCR0A = (uint8_t) (255 - raw_r);
    } else {
        PORTB &= ~(_BV(PB7));
        OCR0A = (uint8_t) (0 - raw_r);
    }
}


void Car::steer(int accel, int yaw) {
    int8_t& h = this -> heading;
    int8_t& s = this -> speed;
    s += accel;
    h += yaw;
    s = (s > 8) ? 8 : (s < -8) ? -8 : s;
    h = (h > 8) ? 8 : (h < -8) ? -8 : h;
    this -> refresh_states();
}

void Car::restore(uint8_t speed, uint8_t heading) {
    if (speed) this -> speed = 0;
    if (heading) this -> heading = 0;
    this -> refresh_states();
}

int Car::get_heading() {
    return this -> heading;
}

int Car::get_speed() {
    return this -> speed;
}

void Car::set_speed(int speed) {
    this -> speed = (speed > 8) ? 8 : (speed < -8) ? -8 : speed;
    this -> refresh_states();
}

void Car::set_heading(int heading) {
    this -> heading = (heading > 8) ? 8 : (heading < -8) ? -8: heading;
    this -> refresh_states();
}
