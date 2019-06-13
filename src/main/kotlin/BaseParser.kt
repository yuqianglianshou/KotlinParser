
abstract class BaseParser<Model> : Runnable{
    companion object {
        //允许网络请求延迟时间，代理IP存在响应速度，这个值过小会过滤掉大量响应速度慢的代理IP
        const val CONNECTION_TIME_OUT:Int = 3000
        //模仿人工操作延迟时间
        const val DELAY_TIME:Long = 2000
    }
    var listener:ParseHtmlListener<Model>? = null
}