#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker run -it --rm --network mynet -v $DIR/../target/scala-2.11/:/jars/ -v $DIR/logs:/logs/ --name spark \
    --env SPARK_PACKAGES=org.apache.spark:spark-sql-kafka-0-10_2.11:2.2.0 \
    --env SPARK_CLASS=spark.kafka.examples.KafkaStreaming \
    --env SPARK_JAR=KafkaStreaming-assembly-0.1.jar \
    --env SPARK_PARAMS="-bootstrap_servers kafka:9092 -input_topic test -output_topic testout" \
    needmorenodes/spark-submit-local:2.2.0
