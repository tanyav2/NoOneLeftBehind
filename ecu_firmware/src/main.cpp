#ifndef F_CPU
#define F_CPU 8000000UL
#endif

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

#include "car.h"

Car car;

int main() {
    while(1) {
        car.set_speed(2);
        _delay_ms(2000);
        car.set_speed(5);
        _delay_ms(2000);
        car.set_speed(8);
        _delay_ms(2000);
        car.restore(1, 0);
        _delay_ms(2000);
        car.set_speed(-8);
        _delay_ms(2000);
        car.restore(1, 0);
        _delay_ms(2000);
        car.set_heading(4);
        _delay_ms(2000);
        car.restore(1, 1);
        _delay_ms(2000);
        car.set_heading(-4);
        _delay_ms(2000);
        car.restore(1, 1);
        _delay_ms(2000);
    }
    //
    // PORTB |= _BV(PB6);
    // PORTB &= ~(_BV(PB7));
    // _delay_ms(2000);
    // PORTB |= _BV(PB7);
    // PORTB &= ~(_BV(PB6));
    // _delay_ms(2000);
}
