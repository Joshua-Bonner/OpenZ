#!/bin/bash
cd /home/pi
git clone https://github.com/Joshua-Bonner/OpenZ.git
cd /home/pi/OpenZ
sudo apt-get install python2.7 openjdk-8-jdk
pip install obd
pip install pillow
pip install pydub
cp /home/pi/OpenZ/Linux-Configs/autostart /etc/xdg/lxsession/LXDE-pi/autostart