
import model.ProxyIpModel
import org.jsoup.Jsoup
import java.net.URL

/**
 * 获取旗云代理免费ip
 * @author  lq
 * @date  2019-06-11 17:05
 */
class ProxyIpParser : BaseParser<ProxyIpModel>() {

    //旗云代理
    private val PROXY_URL = "http://www.qydaili.com/free/?action=china&page="

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Thread(ProxyIpParser()).start()
        }
    }

    override fun run() {
        try {
            val ipList = mutableListOf<ProxyIpModel>()
            //获取旗云前5页的代理IP
            for (page in 1 until 6) {
                println("------- 开始获取代理IP：第 $page 页 -------")
                val proxyDoc = Jsoup.parse(URL(getProxyPageUrl(1)), 2000)

                val tbodies = proxyDoc.body().getElementsByTag("tbody")

                if (tbodies.isNotEmpty()) {
                    val tdBody = tbodies[0]
                    val ipRows = tdBody.getElementsByTag("tr")
                    //解析IP地址
                    for (ipRow in ipRows) {
                        val data_title = ipRow.getElementsByAttribute("data-title")
                        val ip = data_title[0].html()
                        val port = data_title[1].html()
                        ipList.add(ProxyIpModel(ip, port))
                    }
                }
                println("-------  第 $page 页代理IP获取完成 ---------- ")
                //模仿人工点击翻页延迟，防止IP地址被封
                Thread.sleep(DELAY_TIME)

            }
            println("----------------代理ip获取完成---------------- ")
            listener?.onSuccess(ipList)
        } catch (e: Exception) {
            e.printStackTrace()
            listener?.onFailed(e.message ?: "")
        }
    }

    //代理ip 页面
    private fun getProxyPageUrl(page: Int): String {
        return "$PROXY_URL$page"
    }

}