# OpenZ - Spring 2020 Capstone Project HnB Enterprises
## About
OpenZ is an Open Source head unit.  The name comes from the fact that it is Open
Source and it will be installed in a 350Z.

## Features
* Music Player
	* Auto population of Music Library
	* Album Art
	* Auto loading of next song in album
	* Software volume control integrated into player
* GPS
	* Turn by Turn Navigation powered by ARCGIS
	* Note: Due to funding restrictions, this only contains demo
* OBD - On Board Diagnostics
	* Diagnostic Trouble Codes Reading / Clearing
	* Engine Data Output 
* Parking Collision Warning
	* Alerts the driver when they are in danger of hitting an object while parking
	* Contains both Visual and Audio alerts
	* Note: This is a separate unit to the Head Unit itself
* Lane Departure Detection
	* Simple Computer Vision Algorithm detects when the driver is exiting the lanes
	* Alerts driver with a warning signal through the car's speakers

## Hardware
* Raspberry Pi
	* Main Controller for all the software
* Raspberry Pi Camera
	* Used for Lane Departure Detection
* Raspberry Pi Touchscreen
	* Used to display the Head Unit GUI 
	* Used to have touch screen input for GPS, OBD and Music Player
* Arduino Uno
	* Used for Parking Collision Warning
* Ultra Sonic Distance Sensors - HC-SR04
	* Connected to Arduino Uno for Parking Collision Warning Detection
* Passive Buzzer
	* Connected to Arduino Uno for Parking Collision Warning Signal
* Buck Converter 12v to 5V USB Voltage Regulator
	* Used to Draw power from the car to Power all hardware in the vehicle


## Installation
Install scripts can be found in the scripts folder "install.sh"
```bash
sudo chmod +x install.sh
./install.sh
```

## Developers
Alyssa "Siamesegomouw" Abram - https://github.com/Siamesegomouw-PSU
* GPS Research
* GPS Implementation
* Documentation Expert

Ariel "AlimorelManx" Rupp - https://github.com/AlimorelManx
* GPS Research
* GPS Implementation

Jacob "SageTheWizard" Gallucci - https://github.com/SageTheWizard
* Lane Departure Detection
* Music Player
* Auto start Script

Jonathan Edsell - https://github.com/JonEdsell
* UI Developer
* Graphic Design

Joshua Bonner - https://github.com/Joshua-Bonner
* Parking Collision Warning
* Install into vehicle 
* Testing (he owns the 350z)

Richard Bowser - https://github.com/richardb1998
* Hardware Expert
* On Board Diagnostics
* Install Scripts

