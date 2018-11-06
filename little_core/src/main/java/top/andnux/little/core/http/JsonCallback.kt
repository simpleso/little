package top.andnux.little.core.http

abstract class JsonCallback<T> : StringCallback() {

    override fun onSuccess(string: String) {

    }

    abstract fun onSuccess(date: T)
}