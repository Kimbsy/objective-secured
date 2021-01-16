#!/bin/bash

pushd .

cd os-server
./start.sh

cd ../os-ui
./start.sh

popd
