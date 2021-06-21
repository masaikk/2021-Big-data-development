package xyz.masaikk

import scala.io.StdIn
import java.util
import java.util.{Properties, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema


object DoJob {

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


  print("输入查询姓名：")
  //  var user: String =StdIn.readLine()
  var user:String="汤欣欣"

  println("您查询的人员是:"+user)
  val inputTopics: util.ArrayList[String] = new util.ArrayList[String]() {
    {
      add(getInputTopic) //车票购买记录主题
      //add("mn_hotel_stay_1") //酒店入住信息主题
      //add("mn_monitoring_1") //监控系统数据主题
    }
  }
  //val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def getInputTopic: String={
    return "mn_buy_ticket_1"
  }
  /*val kafkaConsumer = new FlinkKafkaConsumer010[ObjectNode](inputTopics,
    new JSONKeyValueDeserializationSchema(true), kafkaProperties)*/
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopics,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)
/*    inputKafkaStream.filter(x => x.get("value").get("username").asText("").equals(user)).map(x => {
      (x.get("metadata").get("topic").asText("") match {
        case "mn_monitoring_1"
        => x.get("value").get("found_time")
        case _ => x.get("value").get("buy_time")
      }, x)
    }).print()*/
//    inputKafkaStream.writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, keyPrefix, period))

//    env.execute()
  }
}
