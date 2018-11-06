package top.andnux.little.core.extend

import com.google.gson.Gson

fun Any?.toJSONString(): String {
    var result = ""
    try {
        val gson = Gson()
        result = gson.toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun Any?.stringValue(): String {
    if (this == null) return ""
    return toString()
}

fun Any?.intValue(): Int {
    if (this == null) return -1
    if (this is Float) return toInt()
    if (this is Double) return toInt()
    if (this is Int) return toInt()
    return -1
}

fun Any?.floatValue(): Float {
    if (this == null) return -1.0f
    if (this is Float) return toFloat()
    if (this is Double) return toFloat()
    if (this is Int) return toFloat()
    return -1.0f
}

fun Any?.doubleValue(): Double {
    if (this == null) return -1.0
    if (this is Float) return toDouble()
    if (this is Double) return toDouble()
    if (this is Int) return toDouble()
    return -1.0
}