#!/bin/bash

stty -F /dev/ttyACM0 9600
nohup java -jar target/uberjar/os-server-0.1.0-SNAPSHOT-standalone.jar > /dev/null 2>&1 &
echo $! > os-server.pid
