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
        int8_t value = server.arg("value").toInt() / 15;
        set_heading_relative(value);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/status", [](){
        Status_vec data = get_status(0);
        String str = String("{") + STOK +
            ", \"steps-traversed\": " + String(data.steps) +
            ", \"obstacle\": " + String(data.obstacles) +
            ", \"sys-status\": " + String(data.status) +
            "}";
        server.send(200, MIME, str);
    });

    server.on("/status-nc", [](){
        Status_vec data = get_status(1);
        String str = String("{") + STOK +
            ", \"steps-traversed\": " + String(data.steps) +
            ", \"obstacle\": " + String(data.obstacles) +
            ", \"sys-status\": " + String(data.status) +
            "}";
        server.send(200, MIME, str);
    });

    server.on("/status/enable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/status/disable-parasitic", [](){
        server.send(404, MIME, TODO);
    });

    server.on("/enable/1", [](){
        spi_exec(0x19);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/enable/2", [](){
        spi_exec(0x1B);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/1", [](){
        spi_exec(0x18);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/2", [](){
        spi_exec(0x1A);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/enable/adas", [](){
        spi_exec(0x11);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/disable/adas", [](){
        spi_exec(0x12);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.on("/status/force-obstacle", [](){
        spi_exec(0x13);
        server.send(200, MIME, String("{")+STOK+"}");
    });

    server.begin();
}
