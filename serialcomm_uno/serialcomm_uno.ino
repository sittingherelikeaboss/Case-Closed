const int sensorPin = A0;
int piezoPin = 8;

void setup() {
  Serial.begin(9600); // Uses pins 0 and 1 (the RX and TX pins)
  Serial.println("<<< Arduino UNO R3 is ready >>>");
}

void loop() {
  int sensorVal = analogRead(sensorPin);
  float voltage = (sensorVal / 1024.0) * 5.0; // convert coltage to temperature in celcius
  float temperature = (voltage - 0.5) * 100;
  Serial.println(temperature);
  delay(500);
}
