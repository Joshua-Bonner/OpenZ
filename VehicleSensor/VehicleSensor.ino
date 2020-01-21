#include "SR04.h"
#define TRIG_PIN 12
#define ECHO_PIN 11
SR04 sr04 = SR04(ECHO_PIN,TRIG_PIN);
long distance;
const int buzz = 13;

void setup() {
   Serial.begin(9600);
   pinMode(buzz, OUTPUT);
}

void loop() {
   distance = sr04.Distance();
   if (distance <= 15){
    tone(buzz, 1000);
    delay(250);
    noTone(buzz);
    delay(250);
   }
   Serial.print(distance);
   Serial.println("cm");
}
