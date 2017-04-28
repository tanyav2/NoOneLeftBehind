# 1 Introduction

## 1.1 Problem Description

<!--TODO: Overall Problem Description-->

The purpose of this design is to ...


## 1.2 Design Concept

<!--TODO: State the overall design concept-->

The project is clearly broken down into two portions: An autonomous car hardware and firmware suite that provides both remote-controlled and autonomous-assistive driving functions, and an Android app that performs photography, image recognition, mapping as well as adds intelligent guidance to the vehicle. 

The Hardware Engineer, Jimmy He, is in charge of the low level control system design, specifically handling the physical aspects of the control mechanism such as circuits, microcontroller firmware, motor driving and stabilization as well as management of sensors.

The Application Engineer, Tanya Verma, is in charge of ... <!--TODO: What Tanya is doing-->

Both portions are interlinked with an API specification stipulated prior to development. This allows maximum flexibility and independence in the development process, and helps contain any possible errors and limitations within a subsystem without rippling towards the entire project. The said APIs are implemented with REST/JSON protocol over HTTP on both sides. 