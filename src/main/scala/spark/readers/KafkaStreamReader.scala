package spark.readers

import org.apache.spark.sql.{DataFrame, SparkSession}

class KafkaStreamReader(spark: SparkSession, kafkaBrokerList: String) {

  def read(topic: Option[String]): DataFrame = {
    topic match {
      case Some(t) =>
        spark.readStream.format("kafka").
          option("kafka.bootstrap.servers", kafkaBrokerList).
          option("subscribe", t).
          load()
      case None =>
        throw new IllegalArgumentException("Missing topic name")
    }
  }
}
