#!/bin/bash

pushd .

cd os-server
./build.sh

cd ../os-ui
./build.sh

popd
