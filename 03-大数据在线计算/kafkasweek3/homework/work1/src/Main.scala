import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer


object Main {
  val target = "b"
  val timeSeconds = 5

  var index = 0
  var times = new ArrayBuffer[Long]()
  val words = new ArrayBuffer[String]()

  case class WordWithCount(word: String, count: Long)

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val text = env.socketTextStream("localhost", 9999)
    val windowCounts = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }
      .map(word => WordWithCount("最近" + timeSeconds + "秒出现含有字符（串）\"" + target + "\"的个数", 1))
      .timeWindowAll(Time.seconds(timeSeconds), Time.seconds(5))
      .sum("count")
    windowCounts.print.setParallelism(1)
    env.execute("Window Stream WordCount")
  }
}