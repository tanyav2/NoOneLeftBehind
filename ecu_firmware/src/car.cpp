#include "car.h"

#define DEBUG 1

Car::Car(uint8_t lf, uint8_t lb, uint8_t rf, uint8_t rb) {
    Control_mux& p = this -> pins;
    p.lf = lf;
    p.lb = lb;
    p.rf = rf;
    p.rb = rb;
    pinMode(lf, OUTPUT);
    pinMode(lb, OUTPUT);
    pinMode(rf, OUTPUT);
    pinMode(rb, OUTPUT);
    this -> refreshStates();
}

Control_data Car::computeControlData() {
    Control_data data;
    int raw_l, raw_r;
    raw_l = (this -> speed) * 32;
    raw_r = raw_l;
    // Calculate heading: negative = left
    raw_l -= 64 * (this -> heading);
    raw_r += 64 * (this -> heading);
    raw_l = (raw_l < -255) ? -255 : raw_l;
    raw_l = (raw_l > 255) ? 255 : raw_l;
    raw_r = (raw_r < -255) ? -255 : raw_r;
    raw_r = (raw_r > 255) ? 255 : raw_r;
    if (raw_l > 0) {
        data.lf = raw_l;
        data.lb = 0;
    } else {
        data.lf = 0;
        data.lb = 0 - raw_l;
    }
    if (raw_r > 0) {
        data.rf = raw_r;
        data.rb = 0;
    } else {
        data.rf = 0;
        data.rb = 0 - raw_r;
    }
    return data;
}

void Car::refreshStates() {
    Control_data data = this -> computeControlData();
    Control_mux& p = this -> pins;
    analogWrite(p.lf, data.lf);
    analogWrite(p.lb, data.lb);
    analogWrite(p.rf, data.rf);
    analogWrite(p.rb, data.rb);
    #ifdef DEBUG
        Serial.print("lf=");
        Serial.print(data.lf);
        Serial.print(" lb=");
        Serial.print(data.lb);
        Serial.print(" rf=");
        Serial.print(data.rf);
        Serial.print(" rb=");
        Serial.print(data.rb);
        Serial.print(" Speed=");
        Serial.print(this -> speed);
        Serial.print(" Heading=");
        Serial.println(this -> heading);
    #endif
}

void Car::steer(int accel, int yaw) {
    int& h = this -> heading;
    int& s = this -> speed;
    s += accel;
    h += yaw;
    s = (s > 8) ? 8 : (s < -8) ? -8 : s;
    h = (h > 8) ? 8 : (h < -8) ? -8 : h;
    this -> refreshStates();
}

void Car::restore(uint8_t speed, uint8_t heading) {
    if (speed) this -> speed = 0;
    if (heading) this -> heading = 0;
    this -> refreshStates();
}

int Car::getheading() {
    return this -> heading;
}

int Car::getspeed() {
    return this -> speed;
}

void Car::setspeed(int speed) {
    this -> speed = (speed > 8) ? 8 : (speed < -8) ? -8 : speed;
    this -> refreshStates();
}

void Car::setheading(int heading) {
    this -> heading = (heading > 8) ? 8 : (heading < -8) ? -8: heading;
    this -> refreshStates();
}
