char incomingByte;
int  LED = 13;
void setup() {
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
  Serial.println("Press 1 to LED ON or 0 to LED OFF");
}

String content = "";
char character;

void loop() {
  //if (Serial.available() > 0)
  //{ incomingByte = Serial.read();
  //  if (incomingByte == 'A') {
  //    digitalWrite(LED, LOW);
  //    Serial.println("LED OFF. Press 1 to LED ON!");
  //  }
  //  if (incomingByte == 'B')
  //  { digitalWrite(LED, HIGH);
  //    Serial.println("LED ON. Press 0 to LED OFF!");
  //  }
  //  else
  //  { Serial.println(incomingByte);
  //  }
  //}

  if (Serial.available())
  {
    //while (Serial.available()) {
      content=Serial.readString();
      
      //character = Serial.read();
      //content.concat(character);
    //}

    if (content != "") {
      Serial.println(content);
    }
  }
}

