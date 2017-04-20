#pragma once

#include <ESP8266WiFi.h>
#include "Arduino.h"

const char *ssid = "NoOneLeftBehind";
const char *password = "ece110final";

IPAddress local_IP(192,168,1,10);
IPAddress gateway(192,168,1,4);
IPAddress subnet(255,255,255,0);

void wifi_init() {
    Serial.print("Initializing Wifi: ");
    Serial.println(
        (WiFi.softAPConfig(local_IP, gateway, subnet) &&
        WiFi.softAP(ssid)) ? "Done" : "Failed:("
    );
    Serial.print("AP's IP Address: ");
    Serial.println(WiFi.softAPIP());
    WiFi.begin("brand 0372", "mnbh7977");
}
