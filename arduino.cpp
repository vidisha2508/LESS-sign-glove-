#include <SoftwareSerial.h>

SoftwareSerial BT(10, 11); // RX, TX

// Flex sensor pins
int flexPins[5] = {A0, A1, A2, A3, A4};

// Thresholds (basic)
int threshold[5] = {30, 13, 20, 20, 100};

// Store binary states
int fingerState[5];

void setup() {
  Serial.begin(9600);
  BT.begin(9600);
}

void loop() {

  // Read sensors and convert to binary
  for (int i = 0; i < 5; i++) {
    int value = analogRead(flexPins[i]);

    if (value > threshold[i]) {
      fingerState[i] = 1;
    } else {
      fingerState[i] = 0;
    }

    // debug raw values
    Serial.print(value);
    if (i < 4) Serial.print(",");
  }
  Serial.println();

  // send binary combination
  for (int i = 0; i < 5; i++) {
    BT.print(fingerState[i]);
  }
  BT.println();

  delay(1000);
}
