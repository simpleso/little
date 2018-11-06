package top.andnux.little.core.http

interface IHttpProxy {

    fun get(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun post(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun postRow(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun update(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun updateRow(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun put(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun putRow(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun delete(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )

    fun download(
        url: String,
        showLoading: Boolean,
        parameter: Map<String, Any>?,
        header: Map<String, String>?,
        callback: IHttpCallback?
    )
}