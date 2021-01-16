#!/bin/bash

lein trampoline dev > /dev/null 2>&1 &
echo $! > os-ui.pid
