#include <Adafruit_NeoPixel.h>

#define S1_PIN 2
#define S1_COUNT 2

Adafruit_NeoPixel strip(S1_COUNT, S1_PIN, NEO_GRB + NEO_KHZ800);

String splitInput[4];

void setup() {
    Serial.begin(9600);

    strip.begin();
    strip.show();

    strip.setPixelColor(0, 255, 0, 0);
    strip.setPixelColor(1, 0, 255, 0);
    strip.show();
}

void loop() {
    if (Serial.available() > 0) {
        String input = Serial.readStringUntil('\n');
        split(input);
        String action = splitInput[0];
        String mission = splitInput[1];
        int index = splitInput[2].toInt();
        String color;
        if (splitInput[0] == "ON") {
            color = splitInput[3];
        } else if (splitInput[0] == "OFF") {
            color = "#000000";
        }
        int r = hexToInt(color.substring(1, 3));
        int g = hexToInt(color.substring(3, 5));
        int b = hexToInt(color.substring(5, 7));
        strip.setPixelColor(index, r, g, b);
        strip.show();
    }
}

String getValue(String data, int index) {
    int found = 0;
    int strIndex[] = {0, -1};
    int maxIndex = data.length()-1;

    for(int i = 0; i <= maxIndex && found <= index; i++){
        if(data.charAt(i) == '|' || i == maxIndex){
            found++;
            strIndex[0] = strIndex[1] + 1;
            strIndex[1] = (i == maxIndex) ? i + 1 : i;
        }
    }

    return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}

void split(String input) {
    splitInput[0] = getValue(input, 0);
    splitInput[1] = getValue(input, 1);
    splitInput[2] = getValue(input, 2);
    splitInput[3] = getValue(input, 3);
}

int hexToInt(String s) {
    byte tens = (s[0] < '9') ? s[0] - '0' : s[0] - '7';
    byte ones = (s[1] < '9') ? s[1] - '0' : s[1] - '7';
    return (int) (16 * tens) + ones;
}
