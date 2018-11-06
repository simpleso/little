package top.andnux.little.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bumptech.glide.Glide

import java.io.BufferedReader
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder


object CommonUtil {

    fun convertToHtml(htmlString: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<![CDATA[")
        stringBuilder.append(htmlString)
        stringBuilder.append("]]>")
        return stringBuilder.toString()
    }

    /**
     * UTF-8编码 转换为对应的 汉字
     *
     * @param s E69CA8
     * @return 木
     */
    fun convertUTF8ToString(s: String?): String? {
        var s = s
        if (s == null || s == "") {
            return null
        }
        try {
            s = s.toUpperCase()
            val total = s.length / 2
            //标识字节长度
            var pos = 0
            val buffer = ByteArray(total)
            for (i in 0 until total) {
                val start = i * 2
                //将字符串参数解析为第二个参数指定的基数中的有符号整数。
                buffer[i] = Integer.parseInt(s.substring(start, start + 2), 16).toByte()
                pos++
            }
            //通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
            //新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
            return String(buffer, 0, pos, Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return s
    }

    /**
     * 汉字 转换为对应的 UTF-8编码
     *
     * @param s 木
     * @return E69CA8
     */
    fun convertStringToUTF8(s: String?): String? {
        if (s == null || s == "") {
            return null
        }
        val sb = StringBuffer()
        try {
            var c: Char
            for (i in 0 until s.length) {
                c = s[i]
                if (c.toInt() >= 0 && c.toInt() <= 255) {
                    sb.append(c)
                } else {
                    val b: ByteArray
                    b = Character.toString(c).toByteArray(charset("utf-8"))
                    for (j in b.indices) {
                        var k = b[j].toInt()
                        //转换为unsigned integer  无符号integer
                        /*if (k < 0)
						k += 256;*/
                        k = if (k < 0) k + 256 else k
                        //返回整数参数的字符串表示形式 作为十六进制（base16）中的无符号整数
                        //该值以十六进制（base16）转换为ASCII数字的字符串
                        sb.append(Integer.toHexString(k).toUpperCase())

                        // url转置形式
                        // sb.append("%" +Integer.toHexString(k).toUpperCase());
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sb.toString()
    }


    fun mapToGetString(map: Map<String, Any>?, cat: String): String {
        val buffer = StringBuffer(cat)
        if (map == null || map.isEmpty()) {
            return ""
        }
        for (key in map.keys) {
            buffer.append(key)
            buffer.append("=")
            buffer.append(StringUtil.toString(map[key]))
            buffer.append("&")
        }
        buffer.deleteCharAt(buffer.length - 1)
        return buffer.toString()
    }

    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    fun getURLDecoderString(str: String?): String {
        var result = ""
        if (null == str) {
            return ""
        }
        try {
            result = URLDecoder.decode(str, Charsets.UTF_8.name())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return result
    }

    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    fun getURLEncoderString(str: String?): String {
        var result = ""
        if (null == str) {
            return ""
        }
        try {
            result = URLEncoder.encode(str, Charsets.UTF_8.name())
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return result
    }

    @SuppressLint("MissingPermission")
    fun callPhone(activity: Activity, phoneNum: String) {
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        activity.startActivity(intent)
    }

    fun changeStatusBarTextColor(window: Window, isBlack: Boolean) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//设置状态栏黑色字体
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE//恢复状态栏白色字体
            }
        }
    }

    fun clearCacheDisk(context: Context): Boolean {
        try {
            FileUtil.deleteDir(context.cacheDir.absolutePath)
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    Glide.get(context).clearDiskCache();
                }).start()
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    fun copyToClipBoard(context: Context, text: String) {
        //获取剪贴板管理器：
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", text)
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.primaryClip = mClipData
        }
    }

    fun getClipBoardText(context: Context): String {
        //获取剪贴板管理器：
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (cm != null) {
            val clip = cm.primaryClip
            return if (clip!!.itemCount > 0) {
                clip.getItemAt(0).text.toString()
            } else {
                ""
            }
        }
        return ""
    }

    fun streamToString(stream: InputStream): String {
        val sb = StringBuilder()
        val br = BufferedReader(InputStreamReader(stream))
        var line: String? = br.readLine()
        try {
            while (line != null) {
                sb.append(line)
                line = br.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close(stream)
        }
        return sb.toString()
    }

    /**
     * 打开软键盘
     *
     * @param mEditText
     * @param mContext
     */
    fun openKeybord(mEditText: EditText, mContext: Context) {
        val imm = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            mEditText.isFocusable = true
            mEditText.isFocusableInTouchMode = true
            mEditText.requestFocus()
            imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    fun closeKeybord(mEditText: EditText, mContext: Context) {
        val imm = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }


    /**
     * 判断当前软键盘是否打开
     *
     * @param activity
     * @return
     */
    fun isSoftInputShow(activity: Activity): Boolean {

        // 虚拟键盘隐藏 判断view是否为空
        val view = activity.window.peekDecorView()
        if (view != null) {
            // 隐藏虚拟键盘
            val inputmanger = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputmanger != null) {
                return inputmanger.isActive && activity.window.currentFocus != null
            }
        }
        return false
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 检查EditText 是否为空
     *
     * @param editTexts
     * @return
     */
    fun checkEditTextEmpty(vararg editTexts: EditText): Boolean {
        for (text in editTexts) {
            if (StringUtil.isEmpty(text.text.toString())) {
                return true
            }
        }
        return false
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    fun close(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    fun getWidth(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.widthPixels
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    fun getHeight(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return metrics.heightPixels
    }

    fun getVersionCode(context: Context): Int {
        var versionCode = 0
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName, 0
            )
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }

    fun getVersionName(context: Context): String {
        var versionName = ""
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName, 0
            )
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionName
    }

    fun getPackgeName(context: Context): String {
        return context.packageName
    }
}
