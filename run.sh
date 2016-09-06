#!/bin/bash
cd "$(dirname "$(realpath "$0")")"; # set script location as working directory
java -cp bin/:lib/jewelcli-0.8.9.jar arden.MainClass "$@"
