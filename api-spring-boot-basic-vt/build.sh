#!/bin/bash
source /c/tools/java21.sh
mvn -U clean package && docker build  -t api-spring-boot-basic-vt .