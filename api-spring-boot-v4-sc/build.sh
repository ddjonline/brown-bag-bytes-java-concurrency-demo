#!/bin/bash
source /c/tools/java25.sh
mvn -U clean package && docker build  -t api-spring-boot-v4-sc .