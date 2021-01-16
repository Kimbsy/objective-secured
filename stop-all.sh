#!/bin/bash

pushd .

cd os-server
./stop.sh

cd ../os-ui
./stop.sh

popd
