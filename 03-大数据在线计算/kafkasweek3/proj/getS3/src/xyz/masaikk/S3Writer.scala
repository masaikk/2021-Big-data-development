package xyz.masaikk

import java.io.{BufferedReader, File, FileReader, FileWriter}
import java.util.{HashMap, LinkedList, Timer, TimerTask}
import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.commons.lang3.StringUtils
import org.apache.flink.api.common.io.OutputFormat
import org.apache.flink.configuration.Configuration

import scala.util.parsing.json.JSON

class S3Writer(accessKey: String, secretKey: String, endpoint: String, bucket: String, keyPrefix: String, period: Int, fileName: String, keyWord: String) extends OutputFormat[String] {
  var timer: Timer = _
  var file: File = _
  var fileWriter: FileWriter = _
  var length = 0L
  var amazonS3: AmazonS3Client = _
  var content = new StringBuilder("")

  def upload(): Unit = {
    this.synchronized {
      if (length > 0) {
        fileWriter.write(group(content.toString))
        fileWriter.flush()
        val targetKey = keyPrefix + System.nanoTime()
        println("开始上传文件：%s 至 %s 桶的 %s 目录下".format(file.getAbsoluteFile, bucket, targetKey))
        amazonS3.putObject(bucket, targetKey, file)
        length = 0L
        content = new StringBuilder("")
      }
    }
  }

  override def configure(configuration: Configuration): Unit = {
    timer = new Timer("S3Writer")
    timer.schedule(new TimerTask() {
      def run(): Unit = {
        upload()
      }
    }, 1000, period)
    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val clientConfig = new ClientConfiguration()
    clientConfig.setProtocol(Protocol.HTTP)
    amazonS3 = new AmazonS3Client(credentials, clientConfig)
    amazonS3.setEndpoint(endpoint)
    file = new File(fileName)
    if (file.exists) {
      val bufferReader = new BufferedReader(new FileReader(file))
      var contentStr: String = null
      while ((contentStr = bufferReader.readLine()) != None && contentStr != null) {
        content.append(contentStr)
      }
      bufferReader.close()
    }
    fileWriter = new FileWriter(file)
  }

  override def open(taskNumber: Int, numTasks: Int): Unit = {
    println("taskNumber:" + taskNumber)
    println("numTasks:" + numTasks)
  }

  override def writeRecord(it: String): Unit = {
    this.synchronized {
      if (StringUtils.isNoneBlank(it)) {
        content.append(it + "\n")
        length += it.length
      }
    }
  }

  override def close(): Unit = {
    fileWriter.flush()
    fileWriter.close()
    timer.cancel()
  }

  def group(lines: String): String = {
    val content = new StringBuilder("")
    val fullContent = new LinkedList[Tuple2[String, String]]
    lines.split("\n")
      .foreach(line => {
        var result = JSON.parseFull(line)
        var new_line: Tuple2[String, String] = null
        result.foreach(m => {
          m match {
            case map: Map[String, Any] => {
              map.get(keyWord) match {
                case Some(x) => new_line = (line, x.toString)
                case _ => new_line = (line, "")
              }
            }
            case _ => {
              // print None
            }
          }
          fullContent.add(new_line)
        })
      })
    val map: HashMap[String, LinkedList[String]] = new HashMap[String, LinkedList[String]]()
    val fileName = new LinkedList[String]()
    var j = 0
    val iter = fullContent.iterator()
    while (iter.hasNext) {
      var next = iter.next()
      if (!map.containsKey(next._2)) {
        var list = new LinkedList[String]()
        list.add(next._1)
        map.put(next._2, list)
        fileName.add(next._2)
      }
      else {
        map.get(next._2).add(next._1)
      }
    }

    for (data <- 0 to fileName.size() - 1) {
      val sortValue = fileName.get(data)
      content.append(sortValue + ":\n")
      // 对于每块数据来说，用sortValue来标识划分
      val list = map.get(sortValue)
      for (index <- 0 to list.size() - 1) {
        content.append(list.get(index) + "\n")
        // 将每一个匹配到的数据加入到content中，待写入文本文件
      }
      content.append("\n")
    }
    content.toString()
  }
}
