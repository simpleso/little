package top.andnux.little.other.wx

import top.andnux.little.other.IOtherListener
import top.andnux.little.other.IOtherProxy
import top.andnux.little.other.OtherType
import top.andnux.little.other.ShareObject

class WxProxy : IOtherProxy.Pay, IOtherProxy.Login, IOtherProxy.Share {

    override fun doPay(
        type: OtherType,
        orderInfo: String,
        listener: IOtherListener
    ) {


    }

    override fun doShared(
        type: OtherType,
        tag: Any,
        shareObject: ShareObject,
        listener: IOtherListener
    ) {

    }

    override fun doLogin(
        type: OtherType,
        listener: IOtherListener
    ) {

    }
}