#!/bin/bash

if [ "$(docker ps -a | grep streamsets-dc)" ]; then
  docker start streamsets-dc 
else
  docker run --network mynet --restart on-failure -p 18630:18630 -d --name streamsets-dc streamsets/datacollector
fi
