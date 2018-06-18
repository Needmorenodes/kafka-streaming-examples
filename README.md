# Kafka Streaming Examples w/ Streamsets

This repo contains
* Scripts and Configs to set up Streamsets and Kafka in Docker
* The Streamsets pipelines to generate and write data to Kafka
* Spark Streaming Examples to Read and Write to/from Kafka

## Setup

### Select Network Device
First run `ifconfig` and find the network device with the internal ip address. For me that was `en0`

Take this device name and put it in `conf/conf.sh` in the `NETWORK_DEVICE` variable.

`NETWORK_DEVICE="en0"`

This allows the other scripts to obtain the correct IP address.

### Set Up Docker Network
Run `docker network create mynet` to create the internal network so the two containers can communicate by the container names.

### Start Containers
With Docker installed, first start the Streamsets and Kafka Containers by running `scripts/start_all.sh`

This will start two containers `kafka` and `streamsets-dc` in docker. 

Streamsets can be reached at `localhost:18630` abd you can log in with `admin:admin`

### Setup Pipelines
After confirming the containers are running with `docker ps` we can import the pipleines to streamsets

First to make sure the pipelines contain the correct ip address run `pipelines/set_ip_pipelines.sh`. This will make new pipeline files in `pipelines/generated` with the correct ip address based on the network device supplied in the conf file.

Now we can go into streamsets and import the pipelines.

* `pipelines/generated/KafkaGeneratePipeline.<IP>.json` and `pipelines/generated/KafkaConsumePipeline.<IP>.json` for the `KafkaStreaming` job.

## Build and Run

### Build
Now that we have Kafka and Streamsets running and generating data, we can build and start the spark job.

Build the job with `sbt clean assembly`

### Run
We can run the jobs with `scripts/spark_submit.sh <Job>`

* `KafkaStreaming` Takes a json object with an ID and a Name field and tries to aggregate Names by ID.

_This script does assume that the spark binaries exist in ~/spark/spark_2.2.0/bin_

_One day I will fix that to be better_

## View Outputs

Now that the jobs are running we can confirm the outputs by using `scripts/consume_topic.sh <topic_name>`

* `test` is the topic the generated data goes to
* `testout` is the topic spark writes to
* `testname` is the topic Streamsets splits off just the names to
* `testid` is the topic Streamsets splits off just the ids to
