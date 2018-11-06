package top.andnux.little.core.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log

import java.io.BufferedWriter
import java.io.File
import java.io.FileFilter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.ArrayList

/**
 * 文件操作类
 */
object FileUtil {

    /**
     * 判断sdcrad是否已经安装
     * @return boolean true安装 false 未安装
     */
    val isSDCardMounted: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * 得到sdcard的路径
     * @return
     */
    val sdCardRoot: String
        get() = if (isSDCardMounted) {
            Environment.getExternalStorageDirectory().absolutePath
        } else ""

    fun getExtention(path: String): String {
        if (StringUtil.isEmpty(path)) {
            return ""
        }
        val i = path.lastIndexOf(".")
        return if (i != -1) {
            path.substring(i)
        } else {
            ""
        }
    }

    fun getExternalDir(path: String): String {
        return if (StringUtil.isEmpty(path)) {
            ""
        } else Environment.getExternalStorageDirectory().absolutePath + File.separator + path
    }

    @JvmStatic
    fun mkDirs(paths: Array<String?>) {
        for (path in paths) {
            val file = File(path)
            if (!file.exists()) {
                file.mkdirs()
            }
        }
    }

    /**
     * 创建文件的路径及文件
     * @param path 路径，方法中以默认包含了sdcard的路径，path格式是"/path...."
     * @param filename 文件的名称
     * @return 返回文件的路径，创建失败的话返回为空
     */
    fun createMkdirsAndFiles(path: String, filename: String): String {
        var path = path
        if (TextUtils.isEmpty(path)) {
            throw RuntimeException("路径为空")
        }
        path = sdCardRoot + path
        val file = File(path)
        if (!file.exists()) {
            try {
                file.mkdirs()
            } catch (e: Exception) {
                throw RuntimeException("创建文件夹不成功")
            }

        }
        val f = File(file, filename)
        if (!f.exists()) {
            try {
                f.createNewFile()
            } catch (e: IOException) {
                throw RuntimeException("创建文件不成功")
            }

        }
        return f.absolutePath
    }

    /**
     * 把内容写入文件
     * @param path 文件路径
     * @param text 内容
     */
    fun write2File(path: String, text: String, append: Boolean) {
        var bw: BufferedWriter? = null
        try {
            // 1.创建流对象
            bw = BufferedWriter(FileWriter(path, append))
            // 2.写入文件
            bw.write(text)
            // 换行刷新
            bw.newLine()
            bw.flush()

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // 4.关闭流资源
            if (bw != null) {
                try {
                    bw.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    /**
     * 删除文件
     * @param path
     * @return
     */
    fun deleteFile(path: String) {
        if (TextUtils.isEmpty(path)) {
            Log.d("FileUtil", "路径为空:$path")
        }
        val file = File(path)
        if (file.exists()) {
            try {
                file.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 删除目录
     * @param path
     */
    fun deleteDir(path: String) {
        if (TextUtils.isEmpty(path)) {
            Log.d("FileUtil", "路径为空:$path")
        }
        val file = File(path)
        if (file.exists()) {//判断文件是否存在
            if (file.isFile) {//判断是否是文件
                file.delete()//删除文件
            } else if (file.isDirectory) {//否则如果它是一个目录
                val files = file.listFiles()//声明目录下所有的文件 files[];
                for (i in files.indices) {//遍历目录下所有的文件
                    deleteDir(files[i].absolutePath)//把每个文件用这个方法进行迭代
                }
                file.delete()//删除文件夹
            }
        } else {
            println("所删除的文件不存在")
        }
    }

    /**
     * 将文件流写入文件
     * @param in
     * @param fileName
     */
    fun copy(inputStream: InputStream, fileName: String) {
        try {
            val fos = FileOutputStream(File(fileName))
            val b = ByteArray(1024)
            var len = inputStream.read(b)
            while (len != -1) {
                fos.write(b, 0, len)
                len = inputStream.read(b)
            }
            fos.flush()
            inputStream.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 拷贝文件
     * @param inFileName
     * @param outFileName
     */
    fun copy(inFileName: String, outFileName: String) {
        try {
            val fos = FileOutputStream(File(outFileName))
            val inputStream = FileInputStream(inFileName)
            copy(inputStream, outFileName)
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 复制Asset目录下文件夹到指定目录
     * @param c
     * @param assetDir
     * @param dir
     * @param f
     */
    fun copyAssetsToDir(c: Context, assetDir: String, dir: String, f: FileFilter?) {
        var f = f
        val files: Array<String>?
        try {
            // 获得Assets一共有几多文件
            files = c.resources.assets.list(assetDir)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        val mWorkingPath = File(dir)
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            // 创建文件夹
            if (!mWorkingPath.mkdirs()) {
                // 文件夹创建不成功时调用
            }
        }
        if (f == null) {
            f = FileFilter { true }
        }
        for (i in files!!.indices) {
            try {
                // 获得每个文件的名字
                val fileName = files[i]
                Log.d("FileUtil", "fileName = $fileName")
                if (f.accept(File(fileName))) {
                    // 根据路径判断是文件夹还是文件
                    if (!fileName.contains(".")) {
                        if (0 == assetDir.length) {
                            copyAssetsToDir(c, assetDir, "$dir$fileName/", f)
                        } else {
                            copyAssetsToDir(
                                c, "$assetDir/$fileName", dir + "/"
                                        + fileName + "/", f
                            )
                        }
                        continue
                    }
                    val outFile = File(mWorkingPath, fileName)
                    if (outFile.exists())
                        outFile.delete()
                    var inputStream: InputStream? = null
                    if (assetDir.isNotEmpty())
                        inputStream = c.assets.open("$assetDir/$fileName")
                    else
                        inputStream = c.assets.open(fileName)
                    val out = FileOutputStream(outFile)
                    // Transfer bytes from in to out
                    val buf = ByteArray(1024)
                    var len: Int = inputStream!!.read(buf)
                    while (len > 0) {
                        out.write(buf, 0, len)
                        out.flush()
                        len = inputStream.read(buf)
                    }
                    inputStream.close()
                    out.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 获得文件夹大小
     * @param file
     * @return
     */
    fun getSize(file: File): Double {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile) {
                //获取文件大小
                val fl = file.listFiles()
                var ss = 0.0
                for (f in fl)
                    ss += getSize(f)
                return ss
            } else {
                val ss = file.length().toDouble() / 1024.0 / 1024.0
                println(file.name + " : " + ss + "MB")
                return ss
            }
        } else {
            println("文件或者文件夹不存在，请检查路径是否正确！")
            return 0.0
        }
    }

    fun getFileList(strPath: String, filter: FileFilter): List<String> {
        return getFileList(null, strPath, filter)
    }

    private fun getFileList(filelist: MutableList<String>?, strPath: String, filter: FileFilter): List<String> {
        var filelist = filelist
        if (filelist == null) {
            filelist = ArrayList()
        }
        val dir = File(strPath)
        val files = dir.listFiles() // 该文件目录下文件全部放入数组
        if (files != null) {
            for (i in files.indices) {
                val fileName = files[i].name
                if (files[i].isDirectory) { // 判断是文件还是文件夹
                    getFileList(filelist, files[i].absolutePath, filter) // 获取文件绝对路径
                } else if (filter.accept(files[i])) { // 判断文件名是否以.avi结尾
                    val strFileName = files[i].absolutePath
                    filelist.add(files[i].absolutePath)
                } else {
                    continue
                }
            }
        }
        return filelist
    }

    /**
     * 获得文件名
     * @param path
     * @return
     */
    fun getFileName(path: String): String {
        var name = ""
        val file = File(path)
        if (file.exists()) {
            name = file.name
        }
        return name
    }

    /**
     * 获得文件扩展名
     * @param path
     * @return
     */
    fun getFileExtension(path: String): String {
        var name = ""
        if (path.contains(".")) {
            val i = path.lastIndexOf(".")
            name = path.substring(i + 1)
        }
        return name
    }
}
