
abstract class BaseParser<Model> : Runnable{
    companion object {
        const val CONNECTION_TIME_OUT:Int = 1000
        const val DELAY_TIME:Long = 2000
    }
    var listener:ParseHtmlListener<Model>? = null
}