package model

/**
 * @author  lq
 * @date  2019-06-11 17:06
 */
data class QuoteModel(
    var content: String,
    var author: String,
    var tags: List<String>
) {
    fun toJson() = """
        {
            "content": $content,
            "author": $author,
            "tags": [${tags.joinToString(separator = ", ")}]
        }
    """.trimIndent()

}