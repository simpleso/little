package top.andnux.little.core.utils

import java.security.MessageDigest
import java.util.Random
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * StringUtil 字符串工具类
 *
 * @author 张春林 2015-1-3 下午2:06:57
 */
object StringUtil {

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    fun checkBankCard(cardId: String): Boolean {
        val bit = getBankCardCheckCode(
            cardId
                .substring(0, cardId.length - 1)
        )
        return if (bit == 'N') {
            false
        } else cardId[cardId.length - 1] == bit
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    fun getBankCardCheckCode(nonCheckCodeCardId: String?): Char {
        if (nonCheckCodeCardId == null
            || nonCheckCodeCardId.trim { it <= ' ' }.length == 0
            || !nonCheckCodeCardId.matches("\\d+".toRegex())
        ) {
            // 如果传的不是数据返回N
            return 'N'
        }
        val chs = nonCheckCodeCardId.trim { it <= ' ' }.toCharArray()
        var luhmSum = 0
        var i = chs.size - 1
        var j = 0
        while (i >= 0) {
            var k = chs[i] - '0'
            if (j % 2 == 0) {
                k *= 2
                k = k / 10 + k % 10
            }
            luhmSum += k
            i--
            j++
        }
        return if (luhmSum % 10 == 0) '0' else (10 - luhmSum % 10 + '0'.toInt()).toChar()
    }

    fun isEmail(email: String): Boolean {
        var isExist = false

        val p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}")
        val m = p.matcher(email)
        val b = m.matches()
        if (b) {
            isExist = true
        }
        return isExist
    }

    /**
     * 判断是否为数字
     *
     * @param str 字符串
     * @return 是：true
     */
    fun isNumeric(str: String): Boolean {
        var i = str.length
        while (--i >= 0) {
            val chr = str[i].toInt()
            if (chr < 48 || chr > 57)
                return false
        }
        return true
    }

    /**
     * 转义
     *
     * @param src 字符串
     * @return 字符串
     */
    fun escape(src: String): String {
        var i: Int
        var j: Char
        val tmp = StringBuffer()
        i = 0
        while (i < src.length) {
            j = src[i]
            if (j == '\'') {
                tmp.append("\\")
            } else if (j == '\\') {
                tmp.append("\\")
            }
            tmp.append(j)
            i++
        }
        return tmp.toString()
    }

    /**
     * 将不定数的几个对象拼接在一起。要求对象重写过toString方法
     *
     * @param param 不定数的对象
     * @return 拼接好的字符串
     */
    fun append(vararg param: Any): String {
        if (param.size == 0) {
            return ""
        }
        val sb = StringBuffer()
        for (temp in param) {
            sb.append(temp)
        }
        return sb.toString()
    }

    /**
     * 判断字符串是否为null和空
     *
     * @param param 字符串
     * @return 既不为空也不为null时，返回false
     */
    fun isEmpty(param: String?): Boolean {
        return !(param != null && "" != param && "null" != param)
    }

    /**
     * 判断字符串是否为null和空
     *
     * @param param 字符串
     * @return 为空或null时，返回false
     */
    fun isNotEmpty(param: String): Boolean {
        return !isEmpty(param)
    }

    /**
     * @param o 传入的对象
     * @return String 转化好的字符串
     * @Title: toString
     * @Description: 将对象转化为String
     */
    fun toString(o: Any?): String {
        return o?.toString() ?: ""
    }

    /**
     * @param o 传入的对象
     * @return int 转化好的数字
     * @Title: toInt
     * @Description: 将对象转化为String
     */
    fun toInt(o: Any?): Int {
        return if (o == null || isEmpty(o.toString())) {
            0
        } else {
            toDouble(o.toString()).toInt()
        }
    }

    fun toIntString(o: Any): String {
        val result = toInt(o)
        return append(result)
    }

    /**
     * @param o 传入的对象
     * @return long 转化好的数字
     * @Title: toLong
     * @Description: 将对象转化为String
     */
    fun toLong(o: Any?): Long {
        return if (o == null || isEmpty(o.toString())) {
            0
        } else {
            toDouble(o.toString()).toLong()
        }
    }

    /**
     * @param o 传入的对象
     * @return double 转化好的数字
     * @Title: toDouble
     * @Description: 将对象转化为String
     */
    fun toDouble(o: Any?): Double {
        return if (o == null || isEmpty(o.toString())) {
            0.0
        } else {
            java.lang.Double.valueOf(o.toString())!!
        }
    }

    /**
     * @param o 传入的对象
     * @return float 转化好的数字
     * @Title: toFloat
     * @Description: 将对象转化为String
     */
    fun toFloat(o: Any?): Float {
        return if (o == null || isEmpty(o.toString())) {
            0f
        } else {
            java.lang.Float.valueOf(o.toString())!!
        }
    }

    /**
     * 将list转化为object数组
     *
     * @param paramList 参数的list
     * @return Object[] 参数的数组
     */
    fun listToArray(paramList: List<Any>): Array<Any?> {
        val o = arrayOfNulls<Any>(paramList.size)
        for (i in paramList.indices) {
            o[i] = paramList[i]
        }
        return o
    }

    /**
     * 在String两边加上 %
     *
     * @param param 需要加百分号的String
     * @return 加好百分号的String
     */
    fun likeString(param: String): String {
        return append("%", param, "%")
    }

    /**
     * 将html转换为code
     *
     * @param s 字符串
     * @return 字符串
     */
    fun htmlToCode(s: String?): String {
        var s = s
        if (s == null) {
            return ""
        } else {
            s = s.replace("\n\r", "<br>&nbsp;&nbsp;")
            s = s.replace("\r\n", "<br>&nbsp;&nbsp;")
            s = s.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
            s = s.replace(" ", "&nbsp;")
            s = s.replace("\"", "\\" + "\"")
            return s
        }
    }

    /**
     * 随机字符串
     *
     * @param length 字符串长度
     * @return 生成的随机字符串
     */
    fun randomString(length: Int): String? {
        var randGen: Random? = null
        var numbersAndLetters: CharArray? = null
        if (length < 1) {
            return null
        }
        if (randGen == null) {
            randGen = Random()
            numbersAndLetters =
                    ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray()
        }
        val randBuffer = CharArray(length)
        for (i in randBuffer.indices) {
            randBuffer[i] = numbersAndLetters!![randGen.nextInt(71)]
        }
        return String(randBuffer)
    }

    /**
     * 随机数字
     *
     * @param length 字符串长度
     * @return 生成的随机数字
     */
    fun randomNumber(length: Int): String? {
        var randGen: Random? = null
        var numbers: CharArray? = null
        if (length < 1) {
            return null
        }
        if (randGen == null) {
            randGen = Random()
            numbers = "0123456789".toCharArray()
        }
        val randBuffer = CharArray(length)
        for (i in randBuffer.indices) {
            randBuffer[i] = numbers!![randGen.nextInt(10)]
        }
        return String(randBuffer)
    }

    /**
     * md5加密
     *
     * @param text 要加密的字符串
     * @return 加密好的字符串
     */
    fun toMd5(text: String): String {
        val HEX_DIGITS = "0123456789ABCDEF".toCharArray()
        val ret = StringBuilder()
        try {
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(text.toByteArray(charset("utf-8")))
            for (i in bytes.indices) {
                ret.append(HEX_DIGITS[(bytes[i].toInt() shr 4) and 0x0f])
                ret.append(HEX_DIGITS[(bytes[i].toInt() and 0x0f)])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret.toString()
    }

    /**
     * 模糊查询包装
     *
     * @param param 模糊查询关键字
     * @return 包装好的模糊查询字段
     */
    fun likeSearch(param: String): String {
        return if (isEmpty(param)) {
            ""
        } else StringUtil.append("%", param, "%")
    }

    /**
     * 去除字符串最后一个字符
     *
     * @param param 参数
     * @return 结果
     */
    fun trimLast(param: String): String {
        return if (isEmpty(param)) {
            ""
        } else param.substring(0, param.length - 1)
    }

    /**
     * 判断手机号码的合理性
     *
     * @param param 参数
     * @return 结果
     */
    fun isPhone(param: String): Boolean {

        var isFlag = false
        if (param.length == 11 && param.startsWith("1")) {
            isFlag = true
        }
        return isFlag
    }

    /**
     * 检测邮箱地址是否合法
     *
     * @param email
     * @return true合法 false不合法
     */
    fun isAEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        val p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")//复杂匹配
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 是否有效的密码
     *
     * @return 不是返回true
     */
    fun isNotValidePwd(pwd: String): Boolean {
        if (isEmpty(pwd)) {
            return true
        } else {
            if (pwd.length < 6 || pwd.length > 29) {
                return true
            }
        }
        return false
    }

    fun toBoolean(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        var value: Boolean? = false
        try {
            value = java.lang.Boolean.valueOf(obj.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return value!!
    }
}
