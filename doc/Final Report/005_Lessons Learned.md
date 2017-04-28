# 4 Conclusion

## 4.1 Lessons Learned

Stipulating an API specification prior to development accelerates the process to a large extent, as the design is deterministic from the get-go, and the architecture can be well-crafted optimized for its respective system without the worry about reworking because of small changes in requirements.

<!--TODO: Insert Lessons on Firmware Engineering-->
The system engineering of the chasis is a clear demonstration of deterministicality and its advantage in designing multi-data-stream, multi-loop real-time control logics. However, the constraints and defects outside of the design intents are also worth-noting. For example, the original design did not account for the discrapency of power outputs between the two motors, yielding a constantly biased travel path. The straight-line calibration routine, which previously used PID turning, didn't account for slips made by the low-quality wheel losing tractions when turning. This issue causes the car to shake violently as the intense wheel slips renders the photogate readings unrepresentative of the actual motion, and ultimately the calibration routine loses control due to invalid input data. This is still an issue in the final design with a slightly fine-tweaked version of the mechanism. The same lesson applies to another _force majeure_, that is the insufficient power output of the geared motors at heavy load and low speed, which heavy engineering efforts are needed to stablize the vehicle against the seemingly irradic motion seen in the lab demonstration.

<!--TODO: Insert Lessons on Android App-->
The API for car control was very useful in designing the Android app and implementing path control as it abstracted away all the system and hardware details. Thus it allowed great flexibility for controlling the car.
