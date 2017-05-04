# 2 Analysis of Components

## 2.1 Characterization of Sensors and Electromechanic Components

### 2.1.1 Reflective Infrared Sensors

The reflective infrared sensors are perfect sensors for detecting the presence of semi-reflective surfaces within a set distance, when surgical precision of that distance is not required. The sensor is implemented by an IR-LED and a photodiode. The LED emits a light beam within the infrared (IR) frequency spectrum, while the photodiode is designed to pick up incoming light of that specific frequency. When the sensor faces an obstacle, the light emitted by the LED will be reflected and sensed by the photodiode, causing it to output a voltage. 

The graph of the voltage output responding to the distance from a sheet of paper is as shown:

![Alt](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/ir.png)

In practice, the sensor module provides an analog comparator (LM393) onboard with a potentiometer, so a present threshold can be set such that the sensor outputs a digital 0 when voltage is below the threshold, and 1 when above. This comparator reduces the load to the microcontroller dramatically, as only one digital bit needs to be processed to alert the precense of obstacle.

### 2.1.2 DC Motor

The DC Motor from the stock ECE110 kit is used to provide power to the vehicle. To control the motor, it is important to understand its I-V characteristic. The I/V characteristics, both at power-up and power-down, are graphed as shown:

![Alt](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/motor.png)

To approximate the graph, the motor exhibits a resistance of 7.8 Ohms when operating, and 0.6 Volts to power on, as well as 0.35 Volts to power down. 


### 2.1.3 Infrared Photogate

The infrared photogate, similar to the reflective infrared sensor, works by picking up the IR light from an LED with a photodiode. However, the photogate shoots the beam straight at the photodiode through a gap, and hence any blockage of the gap can be detected by the absence of IR pickup. This is especially useful to detect the running speeds of the motor with a custom designed rotary encoder installed at the back of the motor. When the motor rotates, the rate of which the encoder blocks the photogate can be measured to deduce the speed.

In the characterization of the encoder, one full rotation of the wheel yields 173 peaks in the resulting square wave, giving a baseline reference for the design of turn and calibration handling.

### 2.1.4 HC-SR04 Ultrasonic Transducer

The HC-SR04 Ultrasonic Sensor is a device designed to measure distance from any object in front, with a typical range from 10 to 120cm. In this project, it is employed to ensure sufficient distance between the vehicle and persons being photographed, which have to fit into the frame. 

The sensor is activated by a short pulse of 10 microseconds at Vcc level on the `Trig` pin. It sets the `Echo` pin high and beams out an ultrasound wave forward and waits until the receiver picks up the reflection, at which it will return `Echo` to ground level. This pulse width can be measured to approximate distance by multiplying the speed of sound. The relation between distance and the pulse width is charted below:

![Alt](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/sr04.png)

Deduced from the graph, the input pulse width `y us` follows an approximate linear relation of $y=0.00291x+2.5$ width respect to distance x in meters.

### 2.1.5 CdS Photoresistors

The photoresistor is used to capture the intensity of ambient light. This is crucial to the phototaking process of the phone attached, as a different lighting condition requires a different shutter speed to take an acceptable image, thus affecting the maximum speed the car can travel at. The CdS photoresistor used by the project is a light-sensitive passive component whose resistance decreases when exposed to stronger light. To translate this into a readable voltage level, a 10K pullup resistor is added (fn)[See ECU Circuit Schematic at Appendix A, R2] The resistance as well as voltage output of the sensor are graphed as shown:

![Alt](https://raw.githubusercontent.com/tanyax/NoOneLeftBehind/develop/doc/Final%20Report/img/cds.png)