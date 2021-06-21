package xyz.masaikk

import java.util.{Properties, UUID}

import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.nlpcn.commons.lang.util.IOUtil

class GetS3(accessKey: String, secretKey: String, endpoint: String, bucket: String, keyPrefix: String, fileName: String, topic: String, bootstrapServers: String) {


  //  val topic = "mn_buy_ticket_1"
  //  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"


  def readFromS3(): String = {
    val myClientConfig = new ClientConfiguration()
    myClientConfig.setProtocol(Protocol.HTTP)
    val amazonS3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey), myClientConfig)
    amazonS3.setEndpoint(endpoint)
    val s3Object = amazonS3.getObject(bucket, fileName)
    IOUtil.getContent(s3Object.getObjectContent, "UTF-8")
  }

  def produceToKafka(s3Content: String): Unit = {
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("acks", "all")
    kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](kafkaProperties)
    val dataArr = s3Content.split("\n")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }

  //从s3读取
  def readFile(): String = {
    val awsCredentials = new BasicAWSCredentials(accessKey, secretKey)
    val clientConfiguration = new ClientConfiguration()
    clientConfiguration.setProtocol(Protocol.HTTP)
    val amazonS3 = new AmazonS3Client(awsCredentials, clientConfiguration)
    amazonS3.setEndpoint(endpoint)
    val s3Object = amazonS3.getObject(bucket, fileName)
    IOUtil.getContent(s3Object.getObjectContent, "UTF-8")
  }
}
