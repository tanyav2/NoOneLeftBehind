#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <Ticker.h>
#include <ESP8266WebServer.h>

#include "ifconfig.h"
#include "api_gateway.h"

Ticker traverse_timer;

#define LED_PIN LED_BUILTIN

void traverse() {
    if (current_speed != 0) {
        reported_steps += step_distance * current_speed;
        cumulative_steps += step_distance * current_speed;
        if (digitalRead(LED_PIN)==HIGH) digitalWrite(LED_PIN, LOW);
        else digitalWrite(LED_PIN, HIGH);
    }
    if (current_speed > 0)
        if (cumulative_steps >= obstacle_threshold || cumulative_steps < 0) {
            cumulative_steps = 0;
            current_obstacle = 1;
            current_speed = 0;
            digitalWrite(LED_PIN, HIGH);
        }
    if (current_speed == 0 && !current_obstacle) digitalWrite(LED_PIN, LOW);
}

void setup() {
    Serial.begin(115200);
    wifi_init();
    setup_routes();
    pinMode(LED_PIN, OUTPUT);
    traverse_timer.attach(0.5, traverse);
}


void loop() {
    server.handleClient();
}
