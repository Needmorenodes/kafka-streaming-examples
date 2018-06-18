package spark.writers

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, SparkSession}

class KafkaStreamWriter(spark: SparkSession, kafkaBrokerList: String, checkpointLocation: String) {

  def write(df: DataFrame, topic: Option[String]): StreamingQuery = {
    topic match {
      case Some(t) =>
        df.writeStream.format("kafka").
          option("kafka.bootstrap.servers", kafkaBrokerList).
          option("topic", t).
          option("checkpointLocation", checkpointLocation).
          outputMode("complete").
          start()
      case None =>
        throw new IllegalArgumentException("Missing topic name")
    }
  }
}
