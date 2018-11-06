package top.andnux.little.core.http

import java.io.InputStream
import java.lang.Exception

interface IHttpCallback {

    fun onSuccess(inputStream: InputStream)

    fun onFail(e: Exception)

}