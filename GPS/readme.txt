The GPS program requires two librarys, ArcGIS Runtime SDK for Java and javafx. The project is setup with a gradle build and a maven file.
It also contains a Mobile Map Package file (.mmpk) of the Greater Los Angeles area that is freely availabe to developers with ArcGIS
access. According to the SDK documentation, it is deployable on Windows, Linux, and Mac platforms.

As the mmpk file is only for Los Angeles, here are two address that I know are in the map: 
      2251 sunset plaza drive, los angeles, ca
      3456 oak glen drive, los angeles, ca
Extensive testing on how to input the addresses has not been done so input the address exactly are they are above with the exception
of captiol and lower case letters.

MockCoordinates is an additional java class for feeding coordinates to the map to simulate getting GPS coordinates from an antenna.

Email sent to Mike Mrsa (PSU) about Mobile Map Package file.

Working on integrating GPS into the UI. To do this, I am not working in the files in the repo. I copy the GPS and UI files into a different
directory in case I mess something up and can not get back to the working code. I am looking into using .jar files for ArcGis and JavaFx as
gradle seems to be causing many issues with merging GPS and UI together. I have an example working with the .jar files, but currently only
for one OS at a time. As the Pi is Linux based, I will most likely boot up my Virtual Machine that runs on Linux, and will continue 
development on that OS.
