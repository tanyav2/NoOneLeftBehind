#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESPAsyncTCP.h>
#include <ESPAsyncWebServer.h>


#define MIME "application/json"
#define TODO "{\"status\":\"Not Implemented\"}"
#define STOK "\"status\": \"ok\""

#define step_distance 160 // Rate of speed per 0.5s per stop
#define obstacle_threshold 32000

uint8_t status_parasitic = 0;

volatile int16_t reported_steps = 0;
volatile int16_t cumulative_steps = 0;
volatile uint8_t current_obstacle = 0;
volatile int8_t current_speed = 0;
volatile uint8_t adas_enable = 1;

AsyncWebServer server(80);
// ESP8266WebServer server(80);

void setup_routes() {
    server.on("/", [](AsyncWebServerRequest *request){
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/brightness", [](AsyncWebServerRequest *request){
        request -> send(404, MIME, TODO);
    });

    server.on("/speed", [](AsyncWebServerRequest *request){
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        request -> send(200, MIME, str);
    });

    server.on("/speed/set-absolute", [](AsyncWebServerRequest *request){
        int8_t value = String(request->getParam("value")->value().c_str()).toInt();
        current_speed = value;
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        request -> send(200, MIME, str);
    });

    server.on("/speed/set-relative", [](AsyncWebServerRequest *request){
        int8_t value = String(request->getParam("value")->value().c_str()).toInt();
        current_speed += value;
        current_speed = (current_speed > 8) ? 8 : ((current_speed < -8) ? -8 : current_speed);
        String str = String("{")+STOK+", \"speed\": "+String(current_speed)+"}";
        request -> send(200, MIME, str);
    });

    server.on("/heading/set-relative", [](AsyncWebServerRequest *request){
        current_obstacle = 0;
        current_speed = 0;
        reported_steps = 0;
        cumulative_steps = 0;
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/status", [](AsyncWebServerRequest *request){
        String str = String("{") + STOK +
            ", \"steps-traversed\": " + String(reported_steps) +
            ", \"obstacle\": " + String(current_obstacle) +
            ", \"sys-status\": 0" +
            "}";
        reported_steps = 0;
        current_obstacle = 0;
        request -> send(200, MIME, str);
    });

    server.on("/status-nc", [](AsyncWebServerRequest *request){
        String str = String("{") + STOK +
            ", \"steps-traversed\": " + String(reported_steps) +
            ", \"obstacle\": " + String(current_obstacle) +
            ", \"sys-status\": 0" +
            "}";
        // Don't clear the flag
        request -> send(200, MIME, str);
    });

    server.on("/status/enable-parasitic", [](AsyncWebServerRequest *request){
        request -> send(404, MIME, TODO);
    });

    server.on("/status/disable-parasitic", [](AsyncWebServerRequest *request){
        request -> send(404, MIME, TODO);
    });

    server.on("/enable/1", [](AsyncWebServerRequest *request){
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/enable/2", [](AsyncWebServerRequest *request){
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/1", [](AsyncWebServerRequest *request){
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/2", [](AsyncWebServerRequest *request){
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/enable/adas", [](AsyncWebServerRequest *request){
        adas_enable = 1;
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/adas", [](AsyncWebServerRequest *request){
        adas_enable = 0;
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/status/force-obstacle", [](AsyncWebServerRequest *request){
        if (current_speed > 0) {
            current_obstacle = 1;
            current_speed = 0;
            cumulative_steps = 0;
        }
        request -> send(200, MIME, String("{")+STOK+"}");
    });

    server.begin();
}
