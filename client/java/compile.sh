#!/bin/sh
LIBPATH=lib
javac -classpath $LIBPATH/*jar *.java
echo 'compile .........ok'
java -classpath lib/json.org.jar:  Client
