package xyz.masaikk

import java.sql.{Connection, DriverManager, PreparedStatement}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._
import java.io.File

object Main {
  val url = "jdbc:mysql://119.23.182.180:3306/mymess"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "srp"
  val password = "srp"

  case class SensorReading(name: String, timestamp: Long, amount: Long)

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val file = new File("")
//    println(file.getAbsolutePath)
    val dataStream: DataStream[String] = env.readTextFile(file.getAbsolutePath+"/work4/src/main/java/xyz/masaikk/testData.txt")
    val stream = dataStream.map(data => {
      val split = data.split(",")
      SensorReading(split(0).trim.toString, split(1).trim.toLong, split(2).trim.toLong)
    })
    stream.addSink(new JDBCSink())
    env.execute()
  }

  /*  def main(args: Array[String]): Unit = {
      val file = new File("")

      println(file.getAbsolutePath)//获取绝对路径
    }*/

  class JDBCSink() extends RichSinkFunction[SensorReading] {

    // 定义sql连接、预编译器
    var conn: Connection = _
    var insertSentence: PreparedStatement = _
    var updateSentence: PreparedStatement = _

    // 初始化，创建连接和预编译语句
    override def open(parameters: Configuration): Unit = {
      super.open(parameters)
      classOf[com.mysql.jdbc.Driver]
      conn = DriverManager.getConnection(url, username, password)
      insertSentence = conn.prepareStatement("INSERT INTO flinktest (name, amount) VALUES (?,?)")
      println("")
      updateSentence = conn.prepareStatement("UPDATE flinktest SET amount = ? WHERE name = ?")

    }

    override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
      updateSentence.setString(2, value.name)
      updateSentence.setLong(1, value.amount)
      println(updateSentence)
      updateSentence.execute()
      if (updateSentence.getUpdateCount == 0) {
        insertSentence.setString(1, value.name)
        insertSentence.setLong(2, value.amount)
        println(insertSentence)
        insertSentence.execute()
      }
    }
    override def close(): Unit = {
      insertSentence.close()
      updateSentence.close()
      conn.close()
    }
  }
}


