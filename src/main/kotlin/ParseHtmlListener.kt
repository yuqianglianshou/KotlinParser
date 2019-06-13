
interface ParseHtmlListener<Model> {
    fun onSuccess(models:MutableList<Model>)
    fun onFailed(errMsg:String)
}