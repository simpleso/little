package top.andnux.little.other

import top.andnux.little.other.alipay.AliPayProxy
import top.andnux.little.other.wx.WxProxy

object OtherManager {

    private var mOtherProxy: IOtherProxy? = null

    fun doPay(
        type: OtherType,
        orderInfo: String,
        listener: IOtherListener
    ) {
        when (type) {
            OtherType.ALIPAY -> {
                mOtherProxy = AliPayProxy()
            }
            OtherType.WX -> {
                mOtherProxy = WxProxy()
            }
            else -> {

            }
        }
        val pay = mOtherProxy as? IOtherProxy.Pay
        pay?.doPay(type, orderInfo, listener)
    }

    fun doShared(
        type: OtherType,
        shareObject: ShareObject,
        listener: IOtherListener
    ) {
        when (type) {
            OtherType.ALIPAY -> {

            }
            OtherType.WX -> {
                mOtherProxy = WxProxy()
            }
            else -> {

            }
        }
        val pay = mOtherProxy as? IOtherProxy.Share
        pay?.doShared(type, shareObject, listener)
    }

    fun doLogin(
        type: OtherType,
        listener: IOtherListener
    ) {

        when (type) {
            OtherType.ALIPAY -> {

            }
            OtherType.WX -> {
                mOtherProxy = WxProxy()
            }
            else -> {

            }
        }
        val pay = mOtherProxy as? IOtherProxy.Login
        pay?.doLogin(type, listener)
    }
}