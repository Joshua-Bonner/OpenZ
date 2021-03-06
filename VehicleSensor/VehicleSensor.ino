#include "SR04.h"
#define TRIG_PIN 12
#define ECHO_PIN 11
#define TRIG_PIN2 10
#define ECHO_PIN2 9
#define TRIG_PIN3 8
#define ECHO_PIN3 7
#define TRIG_PIN4 4
#define ECHO_PIN4 2
#define BLUE 6
#define GREEN 5
#define RED 3

SR04 sensor1 = SR04(ECHO_PIN,TRIG_PIN);
SR04 sensor2 = SR04(ECHO_PIN2,TRIG_PIN2);
SR04 sensor3 = SR04(ECHO_PIN3,TRIG_PIN3);
SR04 sensor4 = SR04(ECHO_PIN4,TRIG_PIN4);

const int buzz = 13;

long distance1;
long distance2;
long distance3;
long distance4;
long minDist;
float buzzTime;
int on = 255;
int off = 0;

void setup() {
   Serial.begin(9600);
   pinMode(buzz, OUTPUT);
   pinMode(RED, OUTPUT);
   pinMode(GREEN, OUTPUT);
   pinMode(BLUE, OUTPUT);
}

void loop() {
   analogWrite(GREEN, on);
   analogWrite(BLUE, off);
   minDist = 1000;
   distance1 = sensor1.Distance();
   distance2 = sensor2.Distance();
   distance3 = sensor3.Distance();
   distance4 = sensor4.Distance();

   if (distance1 != 0 && minDist >= distance1) minDist = distance1;
   if (distance2 != 0 && minDist >= distance2) minDist = distance2;
   if (distance3 != 0 && minDist >= distance3) minDist = distance3;
   if (distance4 != 0 && minDist >= distance4) minDist = distance4;
   if (distance1 == 0 || distance2 == 0 || distance3 == 0 || distance4 == 0){
    analogWrite(BLUE, on);
    tone(buzz, 100);
    delay(500);
    noTone(buzz);
   }

   buzzTime = 150 / 50 + minDist;

   if (minDist <= 75){
    analogWrite(GREEN, off);
    tone(buzz, 2000);
    analogWrite(RED, on);
    delay(buzzTime*5);
    analogWrite(RED, off);
    noTone(buzz);
   }
   
   // Prints out distance via the serial monitor found under the tools tab
   // Press ctrl+shift+m to bring up serial monitor
   Serial.print("Distance1: ");
   Serial.print(distance1);
   Serial.println("cm");
   Serial.print("Distance2: ");
   Serial.print(distance2);
   Serial.println("cm");
   Serial.print("Distance3: ");
   Serial.print(distance3);
   Serial.println("cm");
   Serial.print("Distance4: ");
   Serial.print(distance4);
   Serial.println("cm");
}
