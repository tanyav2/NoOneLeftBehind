#pragma once

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

#include "global_car.h"

/* Obstacle Avoidance
  - Sensor 1: Front barrier, ADC2, Normal = 1
  - Sensor 2: Desk Surface, ADC3, Normal = 0 (in contact)
*/

// Sensor Enable Mask
// 1 Means Enable:       M --543210 L
uint8_t SENS_EN_MASK   = 0b00011000; // TODO: Reenable the sensors
// Sensor Trigger Mask
// 1 Means Norm High:    M --543210 L
uint8_t SENS_TRIG_MASK = 0b00010000;
// Obstacle Detection: SENS_EN_MASK & (SENS_TRIG_MASK ^ SENS_READOUTS)

#define OBSTACLE_REWIND_STEPS -10

void obstacle_init() {
    DDRC &= ~(SENS_EN_MASK); //All Sensors IN
    PORTC &= ~(SENS_EN_MASK);
}

uint8_t obstacle_detect() {
    return SENS_EN_MASK & (SENS_TRIG_MASK ^ PINC);
}

void obstacle_avoid() {
    if (!car.turning)
        if (obstacle_detect() && (car.speed > 0)) {
            car.obstacle_flag = 1;
            car.set_speed(0);
            /*
            car.rewinding = 1;
            car.set_speed(-3);
            car.turn_thresh = OBSTACLE_REWIND_STEPS;
            */
        }
}
