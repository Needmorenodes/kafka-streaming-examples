package spark.readers

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.types.StructType

class CSVReader(spark: SparkSession, hasHeader: Boolean) {

  def read(path: String, schema: Option[StructType]): DataFrame = {
    schema match {
      case Some(s) =>
        spark.read.format("csv")
          .option("header", hasHeader.toString())
          .schema(s)
          .load(path)
      case None =>
        throw new IllegalArgumentException("No Schema Provided")
    }
  }

  def read(path: String, schema: Option[StructType], delimiter: String = ","): DataFrame = {
    schema match {
      case Some(s) =>
        spark.read.format("csv")
          .option("header", hasHeader.toString())
          .option("delimiter", delimiter)
          .schema(s)
          .load(path)
      case None =>
        throw new IllegalArgumentException("No Schema Provided")
    }
  }
}
