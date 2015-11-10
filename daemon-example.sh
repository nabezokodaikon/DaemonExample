#!/bin/bash

MY_JARFILE=/home/nabezokodaikon/develop/daemonexample/target/scala-2.11/DaemonExample-assembly-0.0.1.jar
MY_PID_FILE=/home/nabezokodaikon/develop/daemonexample/daemon-example.pid
MAIN_CLASS=com.github.nabezokodaikon.AppDaemon
LOG_CONF_FILE=/home/nabezokodaikon/develop/daemonexample/target/scala-2.11/classes/logback.xml
prog=daemon-example

RETVAL=0

start() {
  echo -n $"Starting ${prog}:"

	/sbin/jsvc  \
        -home ${JAVA_HOME} \
        -cp ${CLASSPATH}:${MY_JARFILE} \
        -pidfile ${MY_PID_FILE} ${MAIN_CLASS} \
        -Dlogback.configurationFile=${LOG_CONF_FILE}
	RETVAL=$?
	if [[ $RETVAL == 0 ]]; then
		echo -e "\t\t [\033[1;32m  OK  \033[0m]"
	else
		echo -e "\t\t [\033[1;31m  FAILED  \033[0m]"
	fi
	return $RETVAL
}

stop() {
	echo -n $"Stopping ${prog}"

	/sbin/jsvc  -stop -pidfile ${MY_PID_FILE} ${MAIN_CLASS}
	RETVAL=$?
	if [[ $RETVAL == 0 ]]; then
		echo -e "\t\t [\033[1;32m  OK  \033[0m]"
	else
		echo -e "\t\t [\033[1;31m  FAILED  \033[0m]"
	fi
	return $RETVAL
}

case "$1" in
	start)
	start
	;;
	stop)
	stop
	;;
	restart)
	start
	stop
	;;
	*)
	echo $"Usage: ${prog} {start|top}"
	RETVAL=2
esac

exit $RETVAL
