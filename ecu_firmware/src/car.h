#pragma once

struct Control_mux {
    uint8_t lf, lb, rf, rb;
};

struct Control_data {
    uint16_t lf, lb, rf, rb;
};

class Car {
    public:
        Car(uint8_t lf, uint8_t lb, uint8_t rf, uint8_t rb);
        Car(): Car(3,5,6,9) {};
        void refreshStates();
        void steer(int accel, int yaw);
        void restore(uint8_t speed, uint8_t heading);
        void setspeed(int speed);
        int getspeed();
        void setheading(int heading);
        int getheading();
    private:
        Control_mux pins;
        Control_data computeControlData();
        int8_t speed = 0;
        int8_t heading = 0;
};
