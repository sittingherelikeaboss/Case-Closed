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

// HOUSE 
#define WIFI_SSID "NorthstarFIBE"
#define WIFI_PASSWORD "2429210615"

// UNCOMMENT WHEN OUTSIDE THE HOUSE
//#define WIFI_SSID "UFOs"
//#define WIFI_PASSWORD "thebestpug"

#include "HX711.h"

#define DOUT 5 // Pin D1 on the NodeMCU ESP8266
#define CLK 4 // Pin D2 on the NodeMUC ESP8266

float calibration_factor = 23170; 
HX711 scale;

void setup() {  
  Serial.begin(9600);

  //------------------------------//
  // START OF WIFI CONNECTION     // 
  //------------------------------//  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected:");
  Serial.println(WiFi.localIP());
  //------------------------------//
  //   END OF WIFI CONNECTION     // 
  //------------------------------// 


  //------------------------------//
  // START OF WEIGHT SENSOR STUFF // 
  //------------------------------//
  scale.begin(DOUT, CLK);
//  scale.set_scale();
  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  scale.tare(); //Reset the scale to 0

  long zero_factor = scale.read_average(); //Get a baseline reading
  Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.println(zero_factor);
  //----------------------------//
  // END OF WEIGHT SENSOR STUFF //
  //----------------------------//


  //------------------------------//
  // START OF FIREBASE DATABASE   // 
  //------------------------------//
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  //----------------------------//
  // END OF FIREBASE DATABASE   //
  //----------------------------//


  delay(1000);
}

void loop() {
  //------------------------------//
  // START OF WEIGHT SENSOR STUFF // 
  //------------------------------//
  double weight; 
  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  weight = scale.get_units();
  //weight = weight < 0 ? 0 : weight;
  weight = 6.9 + weight; // The luggage weight is 6.9kg so we are adding that to the known weight inside
  Serial.println(weight);
//  Serial.println(scale.get_units(), 2);
  //----------------------------//
  // END OF WEIGHT SENSOR STUFF //
  //----------------------------//


  //------------------------------//
  // START OF FIREBASE DATABASE   // 
  //------------------------------//
  Firebase.setFloat("weight", weight);
  if(Firebase.failed()) {
     Serial.println("Sent data to Firebase failed");
  }
  //----------------------------//
  // END OF FIREBASE DATABASE   //
  //----------------------------//

  scale.power_down();              // put the ADC in sleep mode
  delay(500);
  scale.power_up();
}
