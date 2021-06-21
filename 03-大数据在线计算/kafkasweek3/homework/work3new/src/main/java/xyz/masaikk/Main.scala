package xyz.masaikk

import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.sql.{Connection, DriverManager}
import scala.collection.mutable.ArrayBuffer

object Main {
  val url = "jdbc:mysql://119.23.182.180:3306/mymess"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "srp"
  val password = "srp"
  val topic = "testTopicHJQ"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val mysql_data = readData()
    mysql_data.foreach(println(_))
    produceToKafka(mysql_data)
  }


  def readData(): ArrayBuffer[String] = {
    var connection:Connection = null
    var result=new ArrayBuffer[String]
    try{
      //创建连接
      classOf[com.mysql.jdbc.Driver]
      connection = DriverManager.getConnection(url,username,password)
      val statement = connection.createStatement()
      val resultSet=statement.executeQuery("SELECT username,password from user")
      while (resultSet.next()){
        val username = resultSet.getString("username")
        val password = resultSet.getString("password")
        result.append(s"$username,$password")
      }
      result
    } catch {
      case e:Exception => e.printStackTrace()
        result
    }finally {
      if(connection == null){
        connection.close()
      }
    }

  }

  def produceToKafka(mysql_data: ArrayBuffer[String]): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    for (data <- mysql_data) {
      if (!data.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, data)
        println("开始生产数据：" + data)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }
}
