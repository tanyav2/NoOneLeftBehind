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
        void direct_turn(int angle);
        void update_drive_system();
        int8_t speed = 0;
        int8_t heading = 0;
        int raw_l, raw_r;
        int offset_l=0, offset_r=0;
        volatile int32_t lsteps = 0;
        volatile int32_t rsteps = 0;
        volatile uint8_t obstacle_flag = 0;
        volatile int8_t turning = 0;
        volatile int8_t rewinding = 0;
        volatile int16_t turn_thresh = 0;
};
