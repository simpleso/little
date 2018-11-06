package top.andnux.little.core.extend

fun String?.isNotEmptyString(): Boolean {
    if (this == null) return false
    if (this.isEmpty()) return false
    if (this.toLowerCase() == "null") return false
    return true
}

fun String?.isEmptyString(): Boolean {
    return isNotEmptyString()
}