#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

${DIR}/kafka_start.sh
${DIR}/streamsets_start.sh
