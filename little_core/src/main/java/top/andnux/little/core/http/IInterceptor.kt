package top.andnux.little.core.http

interface IInterceptor {

    fun makeUrl(url: String): String

    fun makeParameter(parameter: Map<String, Any>?): Map<String, Any>?

    fun makeHeader(header: Map<String, String>?): Map<String, String>?

}