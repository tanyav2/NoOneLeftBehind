#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <SPI.h>

#include "spi_ops.h"
#include "ifconfig.h"
#include "api_gateway.h"

void setup() {
    Serial.begin(115200);
    spi_init();
    wifi_init();
    setup_routes();
}

void spi_xmit(uint8_t content) {
    SPI_record data = spi_exec(content);
    Serial.print("  > Speed = ");
    Serial.print(data.speed);
    Serial.print(", Heading = ");
    Serial.println(data.heading);
    delay(2000);
}

void loop() {
    server.handleClient();
}
