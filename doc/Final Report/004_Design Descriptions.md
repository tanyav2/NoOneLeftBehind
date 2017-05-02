# 3 Design Descriptions

<!--TODO: Insert general idea and outline-->

## 3.1 Block Diagrams

This project is highly segragated, as the car hardware and the "intelligent" application remain as independent systems on their own. These systems are developed and tested independently, and communicated with each other over the standard REST/JSON protocol on WiFi.

## 3.1.1 Hardware and Firmware Architecture of the Vehicle

As WiFi communication and processing is a heavy, potentially blocking task, with the additional consideration of high real-time requirements of both the driving and communication systems, the vehicle system is further split into two microsystems.

The first system, **Engine Control Unit (ECU)**, is implemented with custom firmware developmed using `avr-libc` framework for an `ATMega48V` Microcontroller. The ECU performs all timing-critical tasks as well as actions related to driving: Power adjustments, primary obstacle avoidance, managing motor status, performing drive system calibrations for a straight-line motion as well as handling the angle of motion when handling a turn. All the said functions are first packaged into an object-oriented abstraction layer to provide maximum code legibility and comfort of development.

Due to the sheer amount of processing and the time-criticalness of each task, it is unrealistic to use the `Arduino` framework to implement the system. Rather, it optimally utilizes the limited system resources. Both edge-triggered External Interrupts `INT0` and `INT1` are used to react to photogates triggered by motor rotation. `Timer0` is used to output two PWM waveforms used by the motor drive systems. Due to the system depleting the pin counts on the microcontroller, only 2 PWM signals are used in combination with the H-bridge, and two other digital outputs are programmed to control the direction (see schematics). `Timer1` is clocked at a rate to interrupt the system 64 times per second to perform calibration and obstacle scanning subroutines. By comparing the steps data collected by left and right photogates, offsets are issued to the motors in attempt to compensate the error and ensure a straight line. The obstacle avoidance function is implemented over the `PC` bank of the microcontroller's GPIO bus, with up to 5 sensors allowed (only 2 sensors are attached in this project). Individual enabling and configuration of trigger modes are implemented with two masks in code, upon which the subroutine can compute the precense of an obstacle with a maximum of 4 clock cycles. This allows the vehicle to stop in time with an absolutely deterministic responsiveness.

The ECU accepts instructions through the SPI bus in mode 0. Each instruction of driving is encoded into 1 byte and sent through the bus. Upon receiving, an Interrupt Service Routine (ISR) causes the ECU to interpret the bytecode instruction and perform the corresponding action. After which, the ECU composes a buffered 6-bit response sequence, and sends them out one byte at a time with a separate handler under the same ISR.

The second system, **Control Gateway Unit (CGU)**, is implemented with `Arduino` framework running atop the `ESP8266` System-On-Chip (SoC) which includes an Application Processor and a 802.11n Wi-Fi radio on the same die. It provides a Wi-Fi Access Point for the Android phone to connect to, and exposes a list of HTTP endpoints as agreed upon in the API specification. Upon each HTTP request, the CGU encodes the request into a bytecode, and sends it down to the ECU over the SPI bus, and clocks in the response via the duplex byte-exchange mechanism inherent to SPI. It then validates the response and converts it into `JSON`, which is sent back over Wi-Fi. As this is the sole task performed by the CGU and that the SPI communication is performed at a 1MHz clock speed, this execution of each instruction achieves near real-time performance.

![Block Diagram](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/block_diagram.png)

<!--TODO: Insert android app function diagram and descriptions-->

Both systems are highly scalable, as the ECU firmware only takes up 1.9 kbytes out of 4kbytes of onboard flash, and uses a mere 64 bytes out of 512 of RAM to operate. Further, the main program loop of both systems are intentially kept blank, to maintain capacity for further additions.

## 3.1.2 Archeticture of the Android Application

The Android app has four use cases. 

**Start Counting**- Pressing this button starts the car and allows it to intelligently traverse the entire room, avoiding obstacles and edges of tables. A function constantly keeps track of the current position of the car, and stops the car when the car comes back to it's original position. While the car is moving, the camera of the phone is tracking faces by leveraging the FaceDetector class provided by Google's Mobile Vision API. 
Each time a new face is detected, it is added to Firebase Storage, which is an application that lets you store data and if authorized, provides access to a publicly accessible url. 
This face is then compared against a gallery of previously stored images in a Face Recognition API, Kairos API. If it is a match, this information is recorded and uploaded to Firebase Database, which also has information about all the previously stored records and people. If it is not a match, it is recorded as an Anonymous person, and at the end of the counting session, the user, if authorized is prompted to add the details of this person to the database and the Kairos gallery. Otherwise, this image is discarded.

**View previous records**- The previous records can be viewed by loading data from the firebase database into the android app.

**Edit records**- This allows editing and deletion of previously stored records. This feature is only accessible to authorized users.

**Edit people** This allows editing, addition and deletion of the details of each person. These details are stored in different forms both in the Firebase database and the Kairos gallery. This feature is also only accessible to authorized users.

## 3.2 Circuit Schematics

**See Appendix A for circuit schematics of ECU and CGU.**

The ECU and CGU are designed onto two custom-made circuit boards, and snap-fit in a manner similar to Arduino shields. The 16-pin mating connector exposes power, SPI bus, serial debug ports and reset signals of both boards. Extensive measures are added to ensure circuit stability, including 100nF decoupling capacitors on all power pins and a massive 10uF capacitor to filter out voltage spikes for the microcontroller to avoid runtime anomaly. The aforementioned obstacle avoidance sensor bus is connected to an array of 15 pins in a conventional "sensor bus" layout, enabling trivial extensibility of the system.


## 3.3 Mechanical Design and Construction

**See Appendix B for mechanical design charts for all parts.**

The chassis is custom designed to reduce form factor and weight, and are 3D printed in totality. The main body is designed around a powerbank and DC motors distributed free by the ECE110 faculty. It minimizes mechanical instability by lowering the center of gravity. The front and rear of the base plate presents four screw-mounting holes each, housing seperately made brackets for photogates and obstacle avoidance modules. 


## 3.4 Brief Procedure of Operation

To begin operation, one pushes the power switch on the battery pack. The ECU and CGU boots up with a brief self-test procedure, while the CGU presents a Wi-Fi hotspot. One connects to the hotspot with the phone, launches the Android application and mounts the phone on the camera gimbal on top of the chassis.

To begin a face scan, simply push the "Start" button on the phone screen. It begins by querying the car for ambient brightness, and using this value to determine the highest speed the car can go. It then sets the car on the move. The car autonomously calibrates its motion path to ensure a straight line motion is obtained. When a face is detected in the view of the camera, the app commands to stop the vehicle, and takes a better quality picture and uploads the picture to Firebase for facial recognition. During the movement, the app keeps polling the status update API to take note of the distance travelled so far on an internal map.

When the car runs towards the end of the table, the ECU detects the presence of front barrier or the abscence of table surface (for a table without edges) via the two forward-facing IR sensors. This triggers an automatic stop, and an obstacle flag is sent to the application via the CGU. The app issues a command for the car to turn according to a specified angle and stops. Then, the app continues sets the car on the move again, repeating the photo-taking procedure until it has returned to its original position, according to the internal mapping algorithm.

Finally, the car polls an external Facial Recognition API to detect faces from all pictures taken through the journey, and produces a list of people present in the room on its screen.

