package spark.kafka.examples

import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.kohsuke.args4j.{CmdLineParser, Option}
import spark.kafka.schemas.JSONSchema
import spark.readers.KafkaStreamReader
import spark.writers.KafkaStreamWriter

import scala.collection.JavaConverters.seqAsJavaListConverter


object CLIArgs {
  @Option(name = "-bootstrap_servers", required = true, usage = "Sets the kafka bootstrap servers")
  var bootstrap_servers: String = null

  @Option(name = "-input_topic", required = true, usage = "Sets the input topic")
  var input_topic: String = null

  @Option(name = "-output_topic", required = true, usage = "Sets the output topic")
  var output_topic: String = null

  @Option(name = "-checkpoint_location", required = false, usage = "Sets the checkpointint location. Defaults to /tmp/")
  var checkpoint_location: String = "file:///tmp/spark_kafka_checkpoint"
}

object KafkaStreaming {

  def parseCLIArgs(args: Array[String]) = {
    val parser = new CmdLineParser(CLIArgs)
    parser.parseArgument(seqAsJavaListConverter(args.toSeq).asJava)
  }

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("StructuredStreaming")
      .getOrCreate()

    parseCLIArgs(args)

    // Initialize kafka reader
    val bootstrapServers = CLIArgs.bootstrap_servers
    val kafkaReader = new KafkaStreamReader(spark, bootstrapServers)

    // Read topic test
    val df = kafkaReader.read(Some(CLIArgs.input_topic))

    val valueDf = selectValueFromKafka(df)
    val parsedJsonDf = parseJson(valueDf)
    val grouped = groupByID(parsedJsonDf)
    val valued = toValue(grouped)

    // Initialize the stream writer
    val kafkaWriter = new KafkaStreamWriter(spark, bootstrapServers, CLIArgs.checkpoint_location)

    // Write and await termination
    val query = kafkaWriter.write(valued, Some(CLIArgs.output_topic))
    query.awaitTermination()

  }

  def selectValueFromKafka(df: DataFrame): DataFrame = {
    df.selectExpr("CAST(value AS STRING)")
  }

  def parseJson(df: DataFrame): DataFrame = {
    df.withColumn("value", from_json(col("value"), JSONSchema.schema))
      .selectExpr("value.id AS id", "value.name AS name")
  }

  def groupByID(df: DataFrame): DataFrame = {
    df.groupBy("id").agg(collect_list(col("name")).as("names"))
  }

  def toValue(df: DataFrame): DataFrame = {
    df.select(to_json(struct(col("id"), col("names"))).as("value"))
  }
}
