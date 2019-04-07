package webcrawler

import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.Random

object WebCrawler extends App {

  import scala.io.Source
  import org.htmlcleaner.HtmlCleaner
  import java.net.URL

  val url = "http://google.com"
  val cleaner = new HtmlCleaner
  val props = cleaner.getProperties

  val depth = 2;

  def asyncWork(n: String): Future[Unit] = Future {
    blocking {
      Thread.sleep(Random.nextInt(500))
    }
    println(n)
    n
  }

  def iterate(x: String, k: Int): Unit = {
    var node = cleaner.clean(new URL(x))

    var elements = node.getElementsByName("a", true)
    for (elem <- elements) {
      var url1 = elem.getAttributeByName("href")
      if (url1 != null) {
        if (!url1.startsWith("http")) url1 = x + url1;
        asyncWork(url1)
        if (k > 0) iterate(url1, k - 1)
      }
    }
  }

  iterate(url, depth)

  Thread.sleep(1000);
}
