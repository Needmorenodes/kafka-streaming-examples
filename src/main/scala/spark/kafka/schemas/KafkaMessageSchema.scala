package spark.kafka.schemas

import org.apache.spark.sql.types._

object KafkaMessageSchema {
  val schema = StructType(
    Array(
      StructField("key", StringType, nullable = true),   // Kafka sends BinaryType, Stringtype for tests
      StructField("value", StringType, nullable = true), // Kafka sends BinaryType, Stringtype for tests
      StructField("topic", StringType, nullable = true),
      StructField("partition", IntegerType, nullable = true),
      StructField("offset", LongType, nullable = true),
      StructField("timestamp", TimestampType, nullable = true),
      StructField("timestampType", IntegerType, nullable = true)
    )
  )
}