#!/bin/bash
java \
    -jar /home/nabezokodaikon/develop/daemonexample/target/scala-2.11/DaemonExample-assembly-0.0.1.jar \
    -cp com.github.nabezokodaikon.Main \
    -Dlogback.configurationFile=/home/nabezokodaikon/develop/daemonexample/target/scala-2.11/classes/logback.xml
