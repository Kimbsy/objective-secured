#!/bin/bash

stty -F /dev/serial0 19200
lein uberjar
nohup java -jar target/uberjar/os-server-0.1.0-SNAPSHOT-standalone.jar > /dev/null 2>&1 &
echo $! > os-server.pid
