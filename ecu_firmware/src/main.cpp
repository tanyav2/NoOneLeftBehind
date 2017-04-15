#ifndef F_CPU
#define F_CPU 8000000UL
#endif

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

#include "global_car.h"
#include "async.h"


int main() {
    // Initialize Hardware
    spi_init();
    sei(); //Initialize interrupt and Program Start
    while (1);
}
