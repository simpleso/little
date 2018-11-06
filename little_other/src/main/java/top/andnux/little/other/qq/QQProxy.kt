package top.andnux.little.other.qq

import top.andnux.little.other.IOtherListener
import top.andnux.little.other.IOtherProxy
import top.andnux.little.other.OtherType
import top.andnux.little.other.ShareObject

class QQProxy : IOtherProxy.Login, IOtherProxy.Share {

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