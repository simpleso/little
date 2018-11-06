package top.andnux.little.core.extend

import java.io.*

fun InputStream?.convertToString(charsetName: String = "utf-8"): String {
    try {
        val buffer = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var len = this?.read(buffer) ?: 0
        while (len != -1) {
            baos.write(buffer, 0, len)
            baos.flush()
            len = this?.read(buffer) ?: 0
        }
        return baos.toString(charsetName)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

fun InputStream?.writeToFile(name: String) {
    try {
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
        val fos = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var len = this?.read(buffer) ?: 0
        while (len != -1) {
            fos.write(buffer, 0, len)
            fos.flush()
            len = this?.read(buffer) ?: 0
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}