#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Set the network device in the conf.sh file that kafkas IP will bind to
source ${DIR}/../conf/conf.sh

# Assumed spark has been downloaded to ~/spark/
~/spark/spark-2.2.0/bin/spark-submit \
    --master local[1] \
    --packages org.apache.spark:spark-sql-kafka-0-10_2.11:2.2.0 \
    --class spark.kafka.examples.$1 \
    ${DIR}/../target/scala-2.11/KafkaStreaming-assembly-0.1.jar \
    -bootstrap_servers `ipconfig getifaddr ${NETWORK_DEVICE}`:9092 \
    -input_topic test \
    -output_topic testout
