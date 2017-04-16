#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <SPI.h>

#include "car_abstraction.h"

#define MIME "application/json"
#define TODO "{\"status\":\"Not Implemented\"}"
#define STOK "\"status\": \"ok\""

uint8_t status_parasitic = 0;

ESP8266WebServer server(80);

void setup_routes() {
    server.on("/", [](){
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/brightness", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/speed", [](){
        int8_t speed = get_speed();
        String str = String("{")+STOK+", \"speed\": "+String(speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/speed/set-absolute", [](){
        int8_t value = server.arg("value").toInt();
        int8_t speed = set_speed_absolute(value);
        String str = String("{")+STOK+", \"speed\": "+String(speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/speed/set-relative", [](){
        int8_t value = server.arg("value").toInt();
        int8_t speed = set_speed_relative(value);
        String str = String("{")+STOK+", \"speed\": "+String(speed)+"}";
        server.send(200, MIME, str);
    });

    server.on("/heading/set-relative", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/status", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/status/enable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/status/disable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.begin();
}
