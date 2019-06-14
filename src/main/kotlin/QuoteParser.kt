import model.ProxyIpModel
import model.QuoteModel
import org.jsoup.Jsoup

/**
 * 名人名言
 * https://www.lixiaojun.xin/static/courses/kotlin/16-crawler.html#%E7%88%AC%E8%99%AB%E4%BB%8B%E7%BB%8D
 * @author  lq
 * @date  2019-06-11 17:05
 */
class QuoteParser(var proxyIp:ProxyIpModel):BaseParser<QuoteModel> (){
    //该网站是一个国外的网站，专门展示名人名言
    private val PROVINCE_URL = "http://quotes.toscrape.com/page/"

    //数据集
    private val list = mutableListOf<QuoteModel>()
    //当前将要爬的网页页码
    private var currentPage:Int = 1;

    override fun run() {
        try {
            println("代理ip:${proxyIp.ipAddress},端口:${proxyIp.port}")

            //获取旗云前5页的名人名言
            for (page in currentPage until 6) {
                println("------- 开始获取：第 $page 页 -------")
                println("地址为："+getProxyPageUrl(page))
                val document = Jsoup.connect(getProxyPageUrl(page))
                    .proxy(proxyIp.ipAddress, proxyIp.port.toInt())
                    .timeout(CONNECTION_TIME_OUT)
                    .get()

//            println(document)
                val elements = document.select("div.quote")

                elements.forEach {
                    val content = it.selectFirst("span.text").html()
                    val author = it.selectFirst("span>small.author").html()
                    val tagEls = it.select("div.tags>a")
                    val tags = mutableListOf<String>()
                    tagEls.forEach { tag -> tags.add(tag.html()) }
                    list.add(QuoteModel(content = content, author = author, tags = tags))
                }
                //模仿人工点击翻页延迟，防止IP地址被封
                Thread.sleep(DELAY_TIME)
                currentPage++
            }

            listener?.onSuccess(list)

        }catch (e:java.lang.Exception){
            println(e.printStackTrace())
            listener?.onFailed(e.message ?: "")
        }
    }
    //代理ip 页面
    private fun getProxyPageUrl(page: Int): String {
        return "$PROVINCE_URL$page"
    }
}