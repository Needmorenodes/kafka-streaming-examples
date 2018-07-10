#!/bin/bash

docker run --name quickstart.cloudera --network mynet --hostname=quickstart.cloudera --privileged=true -t -i -p 50070:50070 -p 50075:50075 -p 8020:8020 -p 8888:8888 -p 80:80 -p 7180:7180 needmorenodes/clusterdock:5.13 /usr/bin/docker-quickstart
