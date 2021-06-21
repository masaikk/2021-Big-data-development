package xyz.masaikk

import java.util.{Properties, UUID}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

import scala.io.StdIn

object Main {
  val accessKey = "29E2BC5B6851CD32568A"
  val secretKey = "WzVDQTFCMjlBMjU2NzQ4MTlFMTU3MjdDMzMyQTg1"
  //s3地址
  val endpoint = "http://scut.depts.bingosoft.net:29997"
  //上传到的桶
  val bucket = "hujianqiao01"
  //上传文件的路径前缀
  val keyPrefix = "upload/"
  //上传数据间隔 单位毫秒
  val period = 5000
  //输入的kafka主题名称
  val inputTopic = "mn_buy_ticket_1"
  //kafka地址
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  val fileName = "data.txt"
  //导入的数据
  def main(args: Array[String]): Unit = {
    val getS3Ret = new GetS3(accessKey,secretKey,endpoint,bucket,keyPrefix,fileName,inputTopic,bootstrapServers)
    val s3Content = getS3Ret.readFile()
    getS3Ret.produceToKafka(s3Content)

    println("输入分类依据（可以选择username,origin等）：")
    var sortKey: String =StdIn.readLine()
//    var sortKey="username"
    println("针对"+sortKey+"分类")

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    val kafkaProperties = new Properties()

    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)

    inputKafkaStream.writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, keyPrefix, period, fileName,sortKey ))
    // 使用S3Writer写入s3
    env.execute()

  }
}
