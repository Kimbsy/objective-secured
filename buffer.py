import sys
import serial

ser = serial.Serial('/dev/ttyACM0', 9600)
ser.write(sys.argv[1].encode('UTF-8'))
