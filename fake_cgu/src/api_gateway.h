#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>


#define MIME "application/json"
#define TODO "{\"status\":\"Not Implemented\"}"
#define STOK "\"status\": \"ok\""

#define step_distance 160 // Rate of speed per 0.5s per stop
#define obstacle_threshold 32000L

uint8_t status_parasitic = 0;

int16_t reported_steps = 0;
int32_t cumulative_steps = 0;
uint8_t current_obstacle = 0;
int8_t current_speed = 0;


ESP8266WebServer server(80);

void setup_routes() {
    server.on("/", [](){
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/brightness", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/speed", [](){
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/speed/set-absolute", [](){
        int8_t value = server.arg("value").toInt();
        current_speed = value;
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/speed/set-relative", [](){
        int8_t value = server.arg("value").toInt();
        current_speed += value;
        current_speed = (current_speed > 8) ? 8 : ((current_speed < -8) ? -8 : current_speed);
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/heading/set-relative", [](){
        current_obstacle = 0;
        current_speed = 0;
        reported_steps = 0;
        cumulative_steps = 0;
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/status", [](){
        String str = String("{") + STOK +
            ", \"steps-traversed\": " + String(reported_steps) +
            ", \"obstacle\": " + String(current_obstacle) +
            ", \"sys-status\": " + String(1) +
            "}";
        reported_steps = 0;
        current_obstacle = 0;
        server.send(200, MIME, str);
    });

    server.on("/status/enable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/status/disable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.begin();
}
