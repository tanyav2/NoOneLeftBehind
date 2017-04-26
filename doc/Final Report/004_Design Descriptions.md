# 3 Design Descriptions

<!--TODO: Insert general idea and outline-->

## 3.1 Block Diagrams

This project is highly segragated, as the car hardware and the "intelligent" application remain as independent systems on their own. These systems are developed and tested independently, and communicated with each other over the standard REST/JSON protocol on WiFi.

## 3.1.1 Hardware and Firmware Architecture of the Vehicle

As WiFi communication and processing is a heavy, potentially blocking task, with the additional consideration of high real-time requirements of both the driving and communication systems, the vehicle system is further split into two microsystems.

The first system, Engine Control Unit (ECU), is implemented with custom firmware developmed using `avr-libc` framework for an `ATMega48V` Microcontroller. The ECU performs all timing-critical tasks as well as actions related to driving: Power adjustments, primary obstacle avoidance, managing motor status, performing drive system calibrations for a straight-line motion as well as handling the angle of motion when handling a turn. All the said functions are first packaged into an object-oriented abstraction layer to provide maximum code legibility and comfort of development.

Due to the sheer amount of processing and the time-criticalness of each task, it is unrealistic to use the `Arduino` framework to implement the system. Rather, it optimally utilizes the limited system resources. Both edge-triggered External Interrupts `INT0` and `INT1` are used to react to photogates triggered by motor rotation. `Timer0` is used to output two PWM waveforms used by the motor drive systems. Due to the system depleting the pin counts on the microcontroller, only 2 PWM signals are used in combination with the H-bridge, and two other digital outputs are programmed to control the direction (see schematics). `Timer1` is clocked at a rate to interrupt the system 64 times per second to perform calibration and obstacle scanning subroutines. By comparing the steps data collected by left and right photogates, offsets are issued to the motors in attempt to compensate the error and ensure a straight line. The obstacle avoidance function is implemented over the `PC` bank of the microcontroller's GPIO bus, with up to 5 sensors allowed (only 2 sensors are attached in this project). Individual enabling and configuration of trigger modes are implemented with two masks in code, upon which the subroutine can compute the precense of an obstacle with a maximum of 4 clock cycles. This allows the vehicle to stop in time with an absolutely deterministic responsiveness.

The ECU accepts instructions through the SPI bus in mode 0. Each instruction of driving is encoded into 1 byte and sent through the bus. Upon receiving, an Interrupt Service Routine (ISR) causes the ECU to interpret the bytecode instruction and perform the corresponding action. After which, the ECU composes a buffered 6-bit response sequence, and sends them out one byte at a time with a separate handler under the same ISR.

The second system, Control Gateway Unit (CGU), is implemented with `Arduino` framework running atop the `ESP8266` System-On-Chip (SoC) which includes an Application Processor and a 802.11n Wi-Fi radio on the same die. It provides a Wi-Fi Access Point for the Android phone to connect to, and exposes a list of HTTP endpoints as agreed upon in the API specification. Upon each HTTP request, the CGU encodes the request into a bytecode, and sends it down to the ECU over the SPI bus, and clocks in the response via the duplex byte-exchange mechanism inherent to SPI. It then validates the response and converts it into `JSON`, which is sent back over Wi-Fi. As this is the sole task performed by the CGU and that the SPI communication is performed at a 1MHz clock speed, this execution of each instruction achieves near real-time performance.

![Block Diagram](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/block_diagram.png)


<!--TODO: Insert android app function diagram and descriptions-->


Both systems are highly scalable, as the ECU firmware only takes up 1.9 kbytes out of 4kbytes of onboard flash, and uses a mere 64 bytes out of 512 of RAM to operate. Further, the main program loop of both systems are intentially kept blank, to maintain capacity for further additions.

## 3.1.2 Archeticture of the Android Application

## 3.2 Circuit Schematics

The ECU and CGU are designed onto two custom-made circuit boards, and snap-fit in a manner similar to Arduino shields. The 16-pin mating connector exposes power, SPI bus, serial debug ports and reset signals of both boards. Extensive measures are added to ensure circuit stability, including 100nF decoupling capacitors on all power pins and a massive 10uF capacitor to filter out voltage spikes for the microcontroller to avoid runtime anomaly. The aforementioned obstacle avoidance sensor bus is connected to an array of 15 pins in a conventional "sensor bus" layout, enabling trivial extensibility of the system.

<!--TODO: Insert CGU and ECU Schematics and descriptions-->

## 3.3 Mechanical Design and Construction

The chassis is custom designed to reduce form factor and weight. See Appendix B for mechanical design charts for all parts, which are 3D printed in totality. The main body is designed around a powerbank and DC motors distributed free by the ECE110 faculty. It minimizes mechanical instability by lowering the center of gravity. The front and rear of the base plate presents four screw-mounting holes each, housing seperately made brackets for photogates and obstacle avoidance modules. 

<!--TODO: Insert engineering charts and descriptions-->

<!--TODO: Insert finished product photo and descriptions-->