#include <Adafruit_NeoPixel.h>

#define STRIP_PIN 2
#define STRIP_COUNT 36

Adafruit_NeoPixel strip(STRIP_COUNT, STRIP_PIN, NEO_GRB + NEO_KHZ800);

String splitInput[4];

struct mission {
    String name;
    int indices[6];
};

mission missions[] = {
    {"incisive-attack",    {1, 12, 19, 30,  0,  0}},
    {"outriders",          {8, 16, 26, 34,  0,  0}},
    {"encircle",           {5, 13, 23, 31,  0,  0}},
    {"divide-and-conquer", {2, 10, 20, 28,  0,  0}},
    {"crossfire",          {7, 17, 25, 35,  0,  0}},
    {"centre-ground",      {0,  6, 18, 24,  0,  0}},
    {"forward-push",       {4, 14, 22, 32,  0,  0}},
    {"ransack",            {3,  9, 15, 21, 27, 33}},
    {"shifting-front",     {3, 11, 21, 29,  0,  0}}
};

void setup() {
    Serial.begin(9600);

    strip.begin();
    strip.show();

    greenFlash();
    greenFlash();
}

void loop() {
    if (Serial.available() > 0) {
        String input = Serial.readStringUntil('\n');
        split(input);
        String action = splitInput[0];
        String missionName = splitInput[1];
        int index = getIndex(missionName, splitInput[2].toInt());
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

void greenFlash() {
    strip.fill(strip.Color(0, 255, 0), 0, 36);
    strip.show();
    delay(200);
    strip.fill(0, 0, 36);
    strip.show();
    delay(200);
}

int getIndex(String missionName, int objective) {
    mission m;
    for (int i = 0; i < 9; i++) {
        if (missions[i].name == missionName) {
            m = missions[i];
        }
    }
    return m.indices[objective];
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
