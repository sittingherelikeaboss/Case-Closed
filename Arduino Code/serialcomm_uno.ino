#include "HX711.h"

#define DOUT  3
#define CLK  2

HX711 scale(DOUT, CLK);

/*
 * Serial pins are Digital pins 0 and 1 
 * Digital pin 0 TX should go into Digital pin RX 5 / D1
 * Digital pin 1 RX should go into Digital pin TX 
 */


float calibration_factor = 24000; 

//const int tempSensorPin = A0;
//int piezoPin = 8;

void setup() {
  Serial.begin(9600); // Uses pins 0 and 1 (the RX and TX pins)
  Serial.println("<<< Arduino UNO R3 is ready >>>");

  scale.set_scale();
  scale.tare(); //Reset the scale to 0

  long zero_factor = scale.read_average(); //Get a baseline reading
  Serial.print("Zero factor: "); //This can be used to remove the need to tare the scale. Useful in permanent scale projects.
  Serial.println(zero_factor);
}

void loop() {
  //-----------------------------------//
  // START OF TEMPERATURE SENSOR STUFF //
  //-----------------------------------//
//  int tempSensorVal = analogRead(tempSensorPin);
//  float tempVoltage = (tempSensorVal / 1024.0) * 5.0; // convert coltage to temperature in celcius
//  float temperature = (tempVoltage - 0.5) * 100;
//  Serial.print("Voltage: ");
//  Serial.print(tempVoltage);
//  Serial.print(" Temperature: ");
//  Serial.println(temperature);
  //---------------------------------//
  // END OF TEMPERATURE SENSOR STUFF //
  //---------------------------------//



  //------------------------------//
  // START OF WEIGHT SENSOR STUFF // 
  //------------------------------//

  scale.set_scale(calibration_factor); //Adjust to this calibration factor
  Serial.println(scale.get_units(), 1);
 
  //----------------------------//
  // END OF WEIGHT SENSOR STUFF //
  //----------------------------//
//
//  scale.power_down();
//  delay(500);
//  scale.power_up();
}
