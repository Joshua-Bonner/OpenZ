#include "SR04.h"
#define TRIG_PIN 12
#define ECHO_PIN 11
#define TRIG_PIN2 10
#define ECHO_PIN2 9 
SR04 sensor1 = SR04(ECHO_PIN,TRIG_PIN);
SR04 sensor2 = SR04(ECHO_PIN2,TRIG_PIN2);
long distance1;
long distance2;
long buzzTime1;
long buzzTime2;
const int buzz = 13;

void setup() {
   Serial.begin(9600);
   pinMode(buzz, OUTPUT);
}

void loop() {
   distance1 = sensor1.Distance();
   distance2 = sensor2.Distance();
   buzzTime1 =  50 / 50 + distance1;
   buzzTime2 =  50 / 50 + distance2;
   
   // For 1st Sensor
   if (distance1 <= 50){
    tone(buzz, 2000);
    delay(buzzTime1);
    noTone(buzz);
    delay(buzzTime1);
   }
   
   // For 2nd Sensor
   if (distance2 <= 50){
    tone(buzz, 2000);
    delay(buzzTime2);
    noTone(buzz);
    delay(buzzTime2);
   }

   // Prints out distance via the serial monitor found under the tools tab
   // Press ctrl+shift+m to bring up serial monitor
   Serial.print("Distance1: ");
   Serial.print(distance1);
   Serial.println("cm");
   Serial.print("Distance2: ");
   Serial.print(distance2);
   Serial.println("cm");
}
