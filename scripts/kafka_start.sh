#!/bin/bash

# Set the network device in the conf.sh file that kafkas IP will bind to
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source ${DIR}/../conf/conf.sh

if [ "$(docker ps -a | grep kafka)" ]; then
  docker start kafka 
else
  docker run -p 9092:9092 -p 2181:2181 --network mynet --hostname kafka --env ADVERTISED_HOST=kafka --env ADVERTISED_PORT=9092 --name kafka -d spotify/kafka
fi
