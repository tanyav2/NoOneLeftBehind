#ifndef F_CPU
#define F_CPU 8000000UL
#endif

#include <avr/io.h>
#include <util/delay.h>
#include <avr/interrupt.h>

#include "async.h"
#include "global_car.h"


int main() {
    // Initialize Hardware
    spi_init();
    adc_init();
    int_init();
    sei(); //Initialize interrupt and Program Start
    while (1);
}
