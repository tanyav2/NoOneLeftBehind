#pragma once

#include <avr/io.h>

#include "global_car.h"

void calibrate_drive() {
    if (car.speed > 0) {
        car.offset_l = 0;
        car.offset_r = 0;
        if (car.lsteps_sofar > car.rsteps_sofar) {
            car.offset_l = car.lsteps_sofar - car.rsteps_sofar;
            PORTD |= _BV(PD1);
        } else if (car.rsteps_sofar > car.lsteps_sofar) {
            car.offset_r = car.rsteps_sofar - car.lsteps_sofar;
            PORTD &= ~(_BV(PD1));
        }
        car.update_drive_system();
    }
    car.lsteps_sofar = 0;
    car.rsteps_sofar = 0;
}
