package spark.kafka.examples

import org.apache.spark.sql.functions._
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
import spark.SparkSessionSetup
import spark.kafka.schemas.KafkaMessageSchema
import spark.readers.CSVReader

class KafkaStreamingTest extends WordSpec
  with Matchers
  with Eventually
  with BeforeAndAfterAll
  with SparkSessionSetup {

  "KafkaStreamingTest" should {
    " select the value field " in withSparkSession { (sparkSession) =>
      val reader = new CSVReader(sparkSession, hasHeader = true)
      val loader = getClass.getClassLoader
      val df = reader.read(loader.getResource("streaming/fromkafka.csv").getPath, Some(KafkaMessageSchema.schema))

      val justValue = KafkaStreaming.selectValueFromKafka(df)
      justValue.columns.length shouldBe 1
      justValue.columns(0) shouldBe "value"
    }

    " parse json field " in withSparkSession { (sparkSession) =>
      val reader = new CSVReader(sparkSession, hasHeader = true)
      val loader = getClass.getClassLoader
      val df = reader.read(loader.getResource("streaming/fromkafka.csv").getPath, Some(KafkaMessageSchema.schema), delimiter = "|")

      val justValue = KafkaStreaming.selectValueFromKafka(df)
      val parsedJson = KafkaStreaming.parseJson(justValue)
      val arr = parsedJson.collect
      arr.length shouldBe 2

      arr(0).length shouldBe 2
      arr(0)(0) shouldBe 12345
      arr(0)(1) shouldBe "Brian"
    }

    " group by id " in withSparkSession { (sparkSession) =>
      val reader = new CSVReader(sparkSession, hasHeader = true)
      val loader = getClass.getClassLoader
      val df = reader.read(loader.getResource("streaming/fromkafka.csv").getPath, Some(KafkaMessageSchema.schema), delimiter = "|")

      val justValue = KafkaStreaming.selectValueFromKafka(df)
      val parsedJson = KafkaStreaming.parseJson(justValue)
      val groupedByID = KafkaStreaming.groupByID(parsedJson)

      val arr = groupedByID.collect
      arr.length shouldBe 1

      arr(0).length shouldBe 2
      arr(0)(0) shouldBe 12345
      arr(0)(1) shouldBe Array("Brian", "Eric")
    }

    " convert to value " in withSparkSession { (sparkSession) =>
      val reader = new CSVReader(sparkSession, hasHeader = true)
      val loader = getClass.getClassLoader
      val df = reader.read(loader.getResource("streaming/fromkafka.csv").getPath, Some(KafkaMessageSchema.schema), delimiter = "|")

      val justValue = KafkaStreaming.selectValueFromKafka(df)
      val parsedJson = KafkaStreaming.parseJson(justValue)
      val groupedByID = KafkaStreaming.groupByID(parsedJson)
      val valued = KafkaStreaming.toValue(groupedByID)

      valued.show(truncate = false)
      valued.columns.length shouldBe 1
      valued.columns(0) shouldBe "value"
    }

    " parse cli args " in {
      val args = Array("-bootstrap_servers", "localhost:9092", "-input_topic", "test", "-output_topic", "testout")

      KafkaStreaming.parseCLIArgs(args)

      CLIArgs.bootstrap_servers shouldBe "localhost:9092"
      CLIArgs.input_topic shouldBe "test"
      CLIArgs.output_topic shouldBe "testout"
    }
  }
}
