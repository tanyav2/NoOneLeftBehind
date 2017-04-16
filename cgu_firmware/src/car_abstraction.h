#include "spi_ops.h"

#define DEBUG 1

int8_t get_speed() {
    return spi_exec(0x01).speed;
}

int8_t set_speed_absolute(int8_t speed) {
    // Just in case of illegal values
    int8_t value = (speed > 8) ? 8 : ((speed < -8) ? -8 : speed);
    // Re-encode negative value
    value = (value < 0) ? ((~value + 0x01) | 0x20) : value;
    value |= 0x40;
    SPI_record data = spi_exec(value);
    return data.speed;
}

int8_t set_speed_relative(int8_t offset) {
    set_speed_absolute(get_speed()+offset);
}
