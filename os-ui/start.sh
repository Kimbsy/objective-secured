#!/bin/bash

lein run -m shadow.cljs.devtools.cli server app > /dev/null 2>&1 &
echo $! > os-ui.pid
