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
    private:
        int8_t speed = 0;
        int8_t heading = 0;
};
