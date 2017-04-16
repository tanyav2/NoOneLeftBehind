#pragma once
#include <avr/io.h>

class Car {
    public:
        Car();
        void refresh_states();
        void steer(int accel, int yaw);
        void restore(uint8_t speed, uint8_t heading);
        void set_speed(int speed);
        int get_speed();
        void set_heading(int heading);
        int get_heading();
        int32_t lsteps = 0;
        int32_t rsteps = 0;
        uint8_t obstacle_flag = 0;
    private:
        int8_t speed = 0;
        int8_t heading = 0;
};
