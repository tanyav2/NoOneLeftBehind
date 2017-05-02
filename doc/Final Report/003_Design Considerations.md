## 2.2 Design Considerations

<!--TODO: Add general and software design considerations-->

<!--TODO: Add hardware considerations-->

This chapter details several concerns encountered in the design process with brief introduction on how they have been resolved. For specific descriptions of these solutions, refer to Design Descriptions, Circuit Schematics and Mechanical Engineering Charts in Chapter 3.

### 2.2.1 Concerns over Electronic Circuit and Power Systems

The major concern is circuit layout for a massive system involving multiple sensors and submodules, which also has to fit in a relatively small size. The resultant engineering choice includes separating the circuits over to two separate boards: One board contains the Engine Control Unit (ECU), housing the microcontroller and motor driver, while the other contains the Control Gateway Unit (CGU), housing the Wi-Fi Application Processor.

As part of the aforementioned concern, additional issues are present such as allocation of pins given the limitations of microcontroller pin count and functions each pin provides. The layout is performed according to the following strategy:

1. Opting to clock the microcontroller with the internal `8MHz` Calibrated RC Oscillator, effectively freeing `PB6` and `PB7` for general purpose I/O.
2. Saving at least one set of PWM generation pins on the same timer, which in this case became `OC0A` and `OC0B`, hence not wasting too many timers for motor driving.
3. Reserving pins with exclusive functions, such as the four pins critical to `SPI`.
4. Grouping pins used by the same function over to physical viscinity, as well as in the same GPIO bank. This enables vectorized (8-bit) operations for those functions which are much faster.

The end result of this is having only 2 free pins on the microcontroller to spare, that are both also used during a debug session to output useful waveforms for diagnosis.

Having a large number of components as well as connecting high-power circuitry, such as the motor driving to the microcontroller back to back presents great issue in circuit stability. As the experiments show that the good-old 100nF decoupler no longer prevents the circuit noise and spikes from disrupting microcontroller program execution, experiments are performed to give both 100nF over all points in circuit connected to `Vcc`, as well as an extra 2.2uF electrolytic cap over the microcontroller's `AVcc` pin to filter out the noise as much as possible.

### 2.2.2 Concerns over Control System Performance

Real-time performance is important to a control system project like this, especially with multitude of sensors that all produce extremely fast signals. For example, the rotary encoder of motors may trigger the photogate both at a maximum rate of 2kHz, which taxes the ability of the microcontroller to handle - There can't be misses just because the CPU is busy processing some other tasks. To make the problem worse, the microcontroller has to deal with constant input from the Wi-Fi processor whenever instructions come over the APIs, and has to produce both the intended result and a response sent back in signal.

As such, the Arduino framework is far from satisfactory to fulfill the real-time requirements. Instead, the barebone `AVR-LibC` framework is used to optimally leverage every single internal component and interrupt vector to deliver real-time deterministic output.

### 2.2.3 Concerns over Critical Control Functions

Safety and Reliability are the most important aspects of a car, especially with one that carries a phone on top. Missing a report from the obstacle avoidance sensor or receiving late by a split second, the phone will fall off the table and money is lost. As such, the CGU, ECU and Android app all have their own degree of autonomy.

The ECU has total control of fine-grained motor movements. Hence, it only exposes control of speed and heading to the CGU, as well as various statuses of the system, such as how much distance is run and whether there is an obstacle. It tunes the motor to ensure a straight line and automatically stops the car whenever there is an obstacle, without needing instructions from upstream devices. Additionally, as response time over Wi-Fi is not deterministic, there is no way for the Android phone to perform an exact turn based on deltas in motor power outputs. Therefore, the ECU uses a dedicated routine to provide **exact degree turn** based on photogate reading, when a turn command is issued.

### 2.2.4 Concerns over Mechanical Reliability

Needless to explain, the car requires meticulous considerations over mechanical structure, as it will house a massive battery, multiple components and a phone mounted on top. At the same time, it has to accomodate non-standard components such as the ECE110 free motor, as well as various sensors that did not come with geometrical dimensions and reference for attachment design.

The solution is to design every component independently and test each with 3D printing until they guarantee a tight fit. Structually critical parts such as the pylons where motors attach to, and the baseplate that houses the battery, are mated on the CAD software and printed as an integral part. Other parts such as the sensor mounters and phone mounts are printed independently and mated onto the pre-designed screw-hole attachment points.

To ensure stability of the images taken with the phone mounted on top, the top plate is screwed to a gimbal mount salvaged from a portable tripod. The battery is lowered to close to the ground to provide a low center of gravity, similar to the design of Tesla Model S. The top dome covers all the mounting points with screws and nuts, hence doubling itself as a washer for senser mounts, bringing integrity to the entire structure.

