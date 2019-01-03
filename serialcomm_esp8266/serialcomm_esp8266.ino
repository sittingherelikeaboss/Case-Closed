#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>

#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>


#define FIREBASE_HOST "case-closed-d2bed.firebaseio.com"
#define FIREBASE_AUTH "viWefTKMje4gkj0ypNrmnJ9SQXlZ9jsis4GEJYpb"

#define WIFI_SSID "BELL977"
#define WIFI_PASSWORD "2429210615"

#define rxPin 5 // Pin D1
#define txPin 4 // Pin D2

String inString = "";
int piezoPin = 16; // Pin D0

SoftwareSerial wifiSerial(rxPin, txPin); // RX, TX

void setup() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected:");
  Serial.println(WiFi.localIP());
  
  Serial.begin(9600);
  wifiSerial.begin(115200); // We need to connect at NodeMCU default baud rate first
  wifiSerial.println("AT+IPR=9600)");
  delay(1000);
  wifiSerial.end();
  // Start the software serial for communication with the ESP8266
  wifiSerial.begin(9600);
  Serial.println("<<< NodeMCU ESP8266 is ready >>>");
  wifiSerial.println("AT+GMR");
}

void loop() {
  // Read serial input
  if(wifiSerial.available() > 0) {
    int inChar = wifiSerial.read();

    if(inChar != '\n') {
      // As long as the incoming byte is not a newline, convert the incoming byte to a char and add it to the string
      inString += (char)inChar;
    }
    else { // if you get a newline, print the string, then the string's value as a float
      Serial.print("Temperature: ");
      float receivedTemp = inString.toFloat();
      if(receivedTemp > 25) {
        tone(piezoPin, 1000, 100);
      }
      Serial.println(receivedTemp); // incoming string converted to float

      inString = ""; // clear string for new input
    }
  }
}

