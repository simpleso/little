package top.andnux.little.core.config

import android.content.Context
import java.util.ArrayList

import top.andnux.little.core.utils.FileUtil

/**
 * 创建资源目录
 */
object DirConfig {
    var SD_ROOT = FileUtil.sdCardRoot + "/"
    var packageName: String = ""
    var name = packageName.substring(packageName.lastIndexOf("."))
    var HOME = "$SD_ROOT$name/"//
    var HOME_IMAGE = HOME + "image/"
    var HOME_VIDEO = HOME + "video/"
    var HOME_AUDIO = HOME + "audio/"
    var HOME_CRASH = HOME + "crash/"//异常
    var HOME_CACHE = HOME + "cache/"//缓存
    var HOME_DOWNLOAD = HOME + "download/"//下载

    fun init(context: Context) {
        packageName = context.packageName
        val fields = DirConfig::class.java.fields
        val paths = ArrayList<String>()
        for (field in fields) {
            if (field.name.startsWith("HOME")) {
                try {
                    val o = field.get(DirConfig::class.java.javaClass)
                    paths.add(o.toString())
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
        }
        val tmp = arrayOfNulls<String>(paths.size)
        for (i in paths.indices) {
            tmp[i] = paths[i]
        }
        FileUtil.mkDirs(tmp)
    }
}
