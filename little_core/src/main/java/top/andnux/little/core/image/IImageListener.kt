package top.andnux.little.core.image

import java.io.File

interface IImageListener {

    fun onStart() {

    }

    fun onSuccess(file: File) {

    }

    fun onError(e: Throwable) {

    }
}