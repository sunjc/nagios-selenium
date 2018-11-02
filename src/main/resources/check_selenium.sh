#!/bin/bash

export DISPLAY=:1
#xhost +
JAVA_HOME=/opt/java/jdk1.8.0_77

$JAVA_HOME/bin/java -Djava.util.logging.config.file=logging.properties -jar $(dirname $0)/lib/nagios-selenium-1.0.jar $@