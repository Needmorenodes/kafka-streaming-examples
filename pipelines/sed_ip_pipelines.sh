#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source ${DIR}/../conf/conf.sh

echo "Getting the IP for device: ${NETWORK_DEVICE}"
ipaddress=`ipconfig getifaddr ${NETWORK_DEVICE}`

echo "Found IP: ${ipaddress}"

for f in ${DIR}/*Pipeline.json; do cp -v -- "$f" "${f/.json/.${ipaddress}.json}"; done
for f in ${DIR}/*${ipaddress}.json; do mv "$f" "${DIR}/generated/"; done

mysed="s/<MYIP_ADDRESS>/$ipaddress/"
find ${DIR}/generated/ -type f -name "*.${ipaddress}.json" -exec sed -i '' -e $mysed {} +
