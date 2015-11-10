#!/bin/bash
./daemon-example.sh start && wait && tail -f logs/app.log
