#!/bin/bash
source /c/tools/java25.sh
mvn -U clean package && docker build  -t loop-speed-test .