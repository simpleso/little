package top.andnux.little.core.http

import top.andnux.little.core.image.IImageProxy

object HttpManager {

    private var httpProxy: IHttpProxy? = null;
    private var mInterceptors = arrayListOf<IInterceptor>()

    fun init(httpProxy: IHttpProxy): Unit {
        this.httpProxy = httpProxy
    }

    fun add(iInterceptor: IInterceptor) {
        mInterceptors.add(iInterceptor)
    }

    fun get(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.get(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun post(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.post(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun postRow(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.postRow(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun update(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.update(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun updateRow(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.updateRow(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun put(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.put(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun putRow(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.putRow(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun delete(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.delete(mMrl, showLoading, mParameter, mHeader, callback)
    }

    fun download(
        url: String,
        showLoading: Boolean = true,
        parameter: Map<String, Any>? = null,
        header: Map<String, String>? = null,
        callback: IHttpCallback? = null
    ) {
        var mMrl = url
        var mParameter = parameter
        var mHeader = header
        mInterceptors.forEach {
            mMrl = it.makeUrl(mMrl)
            mParameter = it.makeParameter(mParameter)
            mHeader = it.makeHeader(mHeader)
        }
        if (this.httpProxy == null) {
            this.httpProxy = OkHttpProxy()
        }
        this.httpProxy?.download(mMrl, showLoading, mParameter, mHeader, callback)
    }
}