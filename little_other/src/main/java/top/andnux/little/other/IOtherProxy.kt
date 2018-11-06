package top.andnux.little.other

interface IOtherProxy {

    interface Pay : IOtherProxy {
        fun doPay(
            type: OtherType,
            orderInfo: String,
            listener: IOtherListener
        )
    }

    interface Share : IOtherProxy {
        fun doShared(
            type: OtherType,
            tag: Any,
            shareObject: ShareObject,
            listener: IOtherListener
        )
    }

    interface Login : IOtherProxy {
        fun doLogin(
            type: OtherType,
            listener: IOtherListener
        )
    }
}