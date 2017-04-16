#include "spi_ops.h"

#define DEBUG 1

struct Status_vec {
    int32_t steps;
    int16_t obstacles;
    int8_t status;
};

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

Status_vec get_status() {
    Status_vec tmp_vec;
    SPI_record data;
    tmp_vec.status = 0;
    data = spi_exec(0x33);
    tmp_vec.steps = data.speed;
    if (data.payload == 0xffffffff) tmp_vec.status = 1;
    data = spi_exec(0x32);
    tmp_vec.obstacles = (int16_t) data.payload;
    if (data.payload == 0xffffffff) tmp_vec.status = 1;
    return tmp_vec;
}

int8_t set_heading_relative(int8_t heading) {
    int8_t value = (heading > 24) ? 24 : ((heading < -24) ? -24 : heading);
    value = (value < 0) ? ((~value + 0x01) | 0x20) : value;
    value |= 0x80;
    SPI_record data = spi_exec(value);
    return data.heading;
}
