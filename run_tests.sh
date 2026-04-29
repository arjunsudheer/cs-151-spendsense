#!/bin/bash

# Directory for libraries
LIB_DIR="lib"
JUNIT_JAR="$LIB_DIR/junit-platform-console-standalone-1.10.2.jar"

# Compile all source and test files
javac -d bin -cp "$JUNIT_JAR:lib/*" $(find src -name "*.java")

# Run tests using the console standalone; include project `bin` and `lib` jars on the classpath so JavaFX classes are available.
java -cp "$JUNIT_JAR:bin:lib/*" org.junit.platform.console.ConsoleLauncher --scan-classpath
