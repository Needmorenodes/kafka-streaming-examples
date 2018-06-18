package spark.kafka.schemas

import org.apache.spark.sql.types._

object JSONSchema {
  val schema = StructType(
    Array(
      StructField("id", LongType, nullable = true),
      StructField("name", StringType, nullable = true)
    )
  )
}
