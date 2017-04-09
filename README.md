# NoOneLeftBehind

_Project Proposal_

_**ECE110 ABE: Jimmy He, Tanya Verma**_

## Introduction

Attendance taking is an essential way to measure class participation, but it is no easy job to be done well. In whichever way attendance is taken, instructors have to choose between being slow and inaccurate: roll-call of students' names is an extreme waste of time, while fast methods such as sign-up sheets are prone to cheating. However, with the impressive accuracy of facial recognition engines such as Microsoft Face API, that dilemma can finally be obliterated. This project is one such attempt. The goal of the project is to create a robot that automatically traverses around the room and records the face of everyone, and creates a attendance report accordingly. As such, instructors do not have to worry about cheating, and lesson can proceed in no time, even when the robot is doing its job.

## Conceptual Overview

The proposed design consists of a phone mounted on a motorized vehicle. To achieve effective attendance taking, three objectives must be met:

1. To traverse the room, ensuring that everyone is captured.
2. To capture quality pictures, so recognition can happen
3. To avoid obstacles, so the car does not interefere with the class activity.

To accomplish objective 2, we decided for a phone to mounted on the car chassis, since phone cameras shoot good quality pictures, and they can double as a processing platform due to network connectivity and compute power. The phone also has to take into consideration of lighting, and adjusts the scanning speed accordingly so pictures do not appear noisy or blurred. Objective 1 will be provided by the phone keeping a virtual "map" of where it has been, similar to that of a roomba. Objective 3 can be done by having obstacle avoidance on the sensor.

### Key Assumptions

With ease of demonstration in mind, objectives 1 and 3 are fulfilled such that the car moves along the edges of an empty table. Due to the elevation of the table, the car can be easily built to capture faces in sight. Further details involving a real-life classroom setting will be discussed in the last section, `Further Goals`.

## Proposed Solution

### Structural Parts

The proposed design contains a customized 3D-printed chassis that houses the motors. It is one similar to the Shadow Chassis but designed with style. A 3D-printed mount can be affixed to the chassis, and accepts a phone of predefined size at a predifined angle.

### Hardware and Electronics

Apart from the Android phone, the hardware is designed into three separate sections for ease of management. They are referred to as the **Control Gateway Unit (CGU)**, **Power Control Unit (PCU)** and **Power Supply Unit (PSU)**.

The PSU hooks up to a QuickCharge-compatible powerbank via 4-wire USB protocol. It contains a simple resistor divider to signal to the powerbank for a voltage elevation. The signal causes the USB VBus to rise to 12V, eliminating the need for a bulky battery pack to drive motors. The PSU outputs two voltage rails, a +12V for motor control and a +3.3V for I/O and powering the microcontroller.

##### BOM for PSU

Part No | Quantity | Purpose
------- | -------- | ---
Resistors | Several | Voltage divider
Push Button Switch | 2 | Power and Quick Charge 2.0 Activation Switch
5mm LED | 1 | Power-on and Voltage Indicator
USB Micro Female Breakout | 1 | Interfaces to Power Supply
QC2.0 Powerbank | 1 | The battery
Polyfuse | 1 | Disconnects the circuit when overcurrent

The PCU contains an Arduino-compatible ATTiny microcontroller connected to an H-bridge that controls the motors. It takes care of the low-level control features only. The purpose of separating it from CGU is because of the lack of available timers for PWM and frequent blocking events on the CGU which may cripple motor control.

The PCU accepts instructions from the CGU over the SPI protocol and performs engine control accordingly. As an additional function, it doubles as the gateway for analog and time-based sensors, such as the distance sensors and ambient light photoresistors. It supports primitive obstacle avoidance when sensors malfunction or gives out absolutely unacceptable values **as a protective measure**. These values are fed to the CGU over SPI protocol upon request.

##### BOM for PCU

Part No | Quantity | Purpose
------- | -------- | -------
L293D | 1 | Motor H-bridge
Polyfuses | 2 | Disconnects the motor when overcurrent (to protect the PSU)
Motors | 2 | Rotates the wheels
CdS photoresistors | 2 | Detects ambient light
HC-SR06 Ultrasonic Sensor | 5 | Detects distance below and around
HC05 | 1 | Serial debugging interface for PCU

The heart of the CGU is an ESP8266 chip runnnig on the Espressif NONOS SDK framework. It provides high-level abstraction of motor-control functions directly controlling the PCU, as well as ambient light detection and obstacle avoidance **for functional purposes** (as opposed to the PCU's own avoidance rules). At the same time, a timer event captures output from a gyroscope as well as a photoencoder to keep track of the car's movement. The gyroscope is added to calibrate the photoencoder input as well as to detect lateral movement when turning, when photoencoder output is unreliable.

It provides a WiFi endpoint to communicate with the Android phone by exposing a REST/JSON API. With an additional timer event or upon request from the phone, it also reports back movements so the phone can map the path current traversed. Reports and instructions are implemented over standard HTTP.

##### BOM for CGU

Part No | Quantity | Purpose
------- | -------- | -------
ESP-12F | 1 | CPU for the CGU
HC05 | 1 | Serial debugging interface for CGU
GY-521 | 1 | Accelerometer and gyroscope
Photogates | 2 | Measuring the motor rotations

### Software Architecture

The phone, running a customized Android program, captures images using its own camera and detects faces using the Microsoft Face API. It does so by commanding the CGU to move vehicle forward along the edge of the demonstration table. It will notify the CGU to slow down when image quality is unacceptable, and accelerate when capturing is complete at one location.

The phone accepts reports from the CGU about current heading, distance since last report and rotation since last reports. It computes a vectorized map using these reports so it knows how to traverse the room efficiently. When finished, the phone commands the CGU to bring the vehicle to a complete stop, and generates a report of all faces detected. If possible, it would also use the FaceMatch API from Facebook and instructor-provided face sheet to match faces to students' names.

## Projected Timeline

Week | Overall | Structural | Electronics | Android
---- | ------- | ---------- | ----------- | -------
10 | Complete API documentation | Begin design | Complete CGU's API simulator in Python |
11 | Individual component testing | 3D-Prints due | Complete CGU | Complete mapping and facial recognition algorithm
12 | Integration Testing | Fully Assembled | Complete PSU/PCU | Mostly Complete
13 | Torture Testing & Debug | -- | Testing of various failure mode protections and obstacle avoidance | Testing edge causes
14 | Demo

## Further Goals

The car is currently able to drive itself on an elevated surface, so it gets enough altitude to capture faces. However, such a surface (e.g. Tables) restrict the mobility of the vehicle and provents it from achieving a full-room scan. In the further development of this project, the car shall be able to identify a person from the floor. There are three viable approaches to consider:

    1. Optical skewing of the image so faces are still identifiable from the sharp angle
    2. Telescoping camera mount, which elevates the camera
    3. Alternative identification methods, such as picking phone's Bluetooth signals or imaging shoes.

Additionally, the camera gimbal mount is currently fixed on the design. In further attempts of development, the gimbal can be made autonomous.

Lastly, even though the car can be controlled without the prescence of the phone, it still heavily relies on it for its autonomous driving features and image processing. In further stages of the project, processing will be carried out onboard the vehicle with more sophisticated equipments, such as a Tegra or Jetson TX1 board. The car can also be made to sustain high temperature and structural stress, therefore to extend its use into emergency situations, where head-counts are needed without risking rescurers' lives.

These further goals are for reference and demonstration of the scalability of the project, as well as the capacity for further exploration within the design concept. They may or may not be implemented in the timespan of ECE 110, and are not part of the primary objective of this final project.
