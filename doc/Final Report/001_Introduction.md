# 1 Introduction

## 1.1 Problem Description

<!--TODO: Overall Problem Description-->

This	is	a	simple	counting	app	that	takes	a	head	count	of the	people	in	a	room.	It	
then	recognizes	them	uniquely	by	comparing	with previously stored	images	in	a	database,	or	adds	
them	to	the	database	if	they’re	not	already	present at the request of the user. The user can choose to add details about the people identified, and can access and edit these records at a later date.


## 1.2 Design Concept

<!--TODO: State the overall design concept-->

The project has two major portions: An autonomous car hardware and firmware suite that provides both remote-controlled and autonomous-assistive driving functions, and an Android app that performs photography, image recognition, mapping as well as adds intelligent guidance to the vehicle. 

The two portions are linked via a REST API, implemented over HTTP. This API provides an abstraction over the controls of the car, which allows easy manipulation and guidance of the vehicle through the Android app.
