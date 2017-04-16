#pragma once

#include "Arduino.h"
#include <SPI.h>

// #define DEBUG 1 
#define SPI_TIMEOUT 10
#define SPI_END_BYTE 0x0A
#define SPI_START_BYTE 0xCE

#define SPI_SS 4

SPISettings m48setting(1000000, MSBFIRST, SPI_MODE0);

union SPI_record {
    uint8_t raw_value[4];
    int32_t payload;
    struct {
        int16_t speed, heading;
    };
};

void spi_init() {
    SPI.begin();
    digitalWrite(SPI_SS, HIGH);
    pinMode(SPI_SS, OUTPUT);
}

uint8_t spi_byte_transact(uint8_t content) {
    digitalWrite(SPI_SS, LOW);
    SPI.beginTransaction(m48setting);
    uint8_t result = SPI.transfer(content);
    SPI.endTransaction();
    digitalWrite(SPI_SS, HIGH);
    #ifdef DEBUG
        Serial.print("Transferred: ");
        Serial.print(content, BIN);
        Serial.print(", Received: ");
        Serial.println(result, HEX);
    #endif
    return result;
}

SPI_record spi_exec(uint8_t instr) {
    /* Exceptions:
      - 1 = Timeout
      - 2 = Validation Error
    */
    uint8_t spi_receiving = 0;
    SPI_record spi_data;
    uint8_t counter = 0;
    uint8_t tmpbyte;

    // Wait until device is ready
    spi_byte_transact(instr);
    while(!spi_receiving) {
        tmpbyte = spi_byte_transact(0);
        counter ++;
        if (counter > SPI_TIMEOUT) {
            // Exception 1
            spi_data.payload = 0xffffffff;
            return spi_data;
        }
        if (tmpbyte == SPI_START_BYTE) spi_receiving = 1;
    }
    // Now Receiving Data
    for (uint8_t i=0; i<4; i++) {
        spi_data.raw_value[i] = spi_byte_transact(0);
    }
    // Done Receiving. Validate.
    tmpbyte = spi_byte_transact(0);
    if ((tmpbyte & SPI_END_BYTE) != SPI_END_BYTE) {
        spi_data.payload = 0xffffffff;
        return spi_data;
    }
    #ifdef DEBUG
        Serial.print("Received Data: ");
        Serial.println(spi_data.payload, HEX);
    #endif
    return spi_data;
}
