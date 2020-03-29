#!/bin/bash
cd /home/pi
git clone https://github.com/Joshua-Bonner/OpenZ.git
cd /home/pi/OpenZ
sudo apt-get install python2.7 openjdk-8-jdk
pip install obd
pip install pillow
pip install pydub
sudo echo "python /home/pi/OpenZ/Lane_Departure_Detection/gafbm.py &" >> /etc/rc.local
sudo echo "python /home/pi/OpenZ/OBD/OBDServer/OBDServer.py &" >> /etc/rc.local 
sudo echo "java -jar  /home/pi/OpenZ/UI/out/artifacts/UI_jar/UI.jar &" >> /etc/rc.local #TODO add path of JAR File