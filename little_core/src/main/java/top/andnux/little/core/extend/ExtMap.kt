package top.andnux.little.core.extend

fun Map<String, Any>?.toGetString(): String {
    var buffer = StringBuffer()
    this?.forEach {
        buffer.append(it.key)
        buffer.append("=")
        buffer.append(it.value.toString())
        buffer.append("&")
    }
    if (buffer.endsWith("&")) {
        buffer.deleteCharAt(buffer.length - 1)
    }
    return buffer.toString()
}

fun Map<String, Any>?.stringValue(key: String): String {
    val any: Any? = this?.get(key) ?: return ""
    return any.toString()
}

fun Map<String, Any>?.intValue(key: String): Int {
    val any: Any? = this?.get(key) ?: return -1
    if (any is Float) return any.toInt()
    if (any is Double) return any.toInt()
    if (any is Int) return any.toInt()
    return -1
}

fun Map<String, Any>?.floatValue(key: String): Float {
    val any: Any? = this?.get(key) ?: return -1.0f
    if (any is Float) return any.toFloat()
    if (any is Double) return any.toFloat()
    if (any is Int) return any.toFloat()
    return -1.0f
}

fun Map<String, Any>?.doubleValue(key: String): Double {
    val any: Any? = this?.get(key) ?: return -1.0
    if (any is Float) return any.toDouble()
    if (any is Double) return any.toDouble()
    if (any is Int) return any.toDouble()
    return -1.0
}