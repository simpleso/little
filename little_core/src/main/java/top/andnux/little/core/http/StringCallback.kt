package top.andnux.little.core.http

import top.andnux.little.core.extend.convertToString
import top.andnux.little.core.utils.CommonUtil
import java.io.InputStream

abstract class StringCallback : IHttpCallback {

    override fun onSuccess(inputStream: InputStream) {
        val string = inputStream.convertToString()
        onSuccess(string)
    }

    abstract fun onSuccess(string: String)
}