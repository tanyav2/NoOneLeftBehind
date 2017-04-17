#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <Ticker.h>
#include <ESP8266WebServer.h>

#include "ifconfig.h"
#include "api_gateway.h"

Ticker traverse_timer;

void traverse() {
    reported_steps += step_distance * current_speed;
    cumulative_steps += step_distance;
    if (cumulative_steps >= obstacle_threshold) {
        cumulative_steps = 0;
        current_obstacle = 1;
        current_speed = 0;
    }
}

void setup() {
    Serial.begin(115200);
    wifi_init();
    setup_routes();
}


void loop() {
    server.handleClient();
    traverse_timer.attach(0.5, traverse);
}
