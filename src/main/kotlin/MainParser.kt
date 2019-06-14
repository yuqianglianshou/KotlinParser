import model.ProxyIpModel
import model.QuoteModel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * @author  lq
 * @date  2019-06-11 14:06
 */
class MainParser {

    //伴生对象
    companion object {
        //代理IP地址集合
        var list_proxyip: MutableList<ProxyIpModel> = mutableListOf()

        val threadExecutor = ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2,
            Int.MAX_VALUE,
            2000,
            TimeUnit.MICROSECONDS,
            LinkedBlockingQueue()
        )

        //JvmStatic 在类中生成set()get()方法
        @JvmStatic
        fun main(args: Array<String>) {

            //1,获取旗云免费代理IP
            val proxyIpParser = ProxyIpParser()

            //监听
            proxyIpParser.listener = object : ParseHtmlListener<ProxyIpModel> {

                override fun onSuccess(ipProxys: MutableList<ProxyIpModel>) {

                    list_proxyip = ipProxys
                    println("代理IP数量：\n${list_proxyip.size}")
                    println("代理IP：\n${list_proxyip.toString()}")

                    val mainParser = MainParser()
                    mainParser.parse()

                }

                override fun onFailed(errMsg: String) {
                    //第一次请求会出现超时异常，在此重新请求
//                    println("onFailed =  $errMsg")
                    threadExecutor.execute(proxyIpParser)
                }

            }

            //执行爬虫
            threadExecutor.execute(proxyIpParser)

        }

        /**
         * 随机选择一个代理IP
         */
        fun getRandomProxyIp(): ProxyIpModel {
            val random = Random.nextInt(list_proxyip.size)
            return list_proxyip[random]
        }
    }

    /**
     * 爬取前5页的名人数据
     */
    fun parse(){
        val quoteThread = QuoteParser(getRandomProxyIp())

        //监听
        quoteThread.listener = object : ParseHtmlListener<QuoteModel> {
            override fun onSuccess(list: MutableList<QuoteModel>) {
                //输出json
//                println(list)
                println("爬取的名人名言数量："+list.size)
                println("爬取的名人名言：")
                list.forEach{
                    //只输出内容
                    println(it.content)
                }
            }

            override fun onFailed(erMsg: String) {
                println("erMsg = [$erMsg]")
                //爬取失败，再次随机取一个IP请求
                //第一次请求失败或者代理IP不可用超时等
                //有时我们获取的免费IP大部分不可用，会出现很多
                //解析url:http://quotes.toscrape.com/
                //代理ip:221.7.255.168,端口:80
                //erMsg = [Read timed out]
                //这种情况，请耐心等会。
                quoteThread.proxyIp = getRandomProxyIp()
                //再次执行
                threadExecutor.execute(quoteThread)
            }
        }
        //执行
        threadExecutor.execute(quoteThread)
    }

}