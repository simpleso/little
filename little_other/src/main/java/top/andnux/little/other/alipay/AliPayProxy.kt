package top.andnux.little.other.alipay

import android.os.Handler
import android.os.Looper
import android.os.Message
import top.andnux.little.other.IOtherListener
import top.andnux.little.other.IOtherProxy
import top.andnux.little.other.OtherType
import top.andnux.little.other.ShareObject
import com.alipay.sdk.app.PayTask
import top.andnux.little.core.manager.AppActivityManager
import top.andnux.little.core.extend.stringValue


class AliPayProxy : IOtherProxy.Pay {

    private var listener: IOtherListener? = null

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (msg?.what == 0x999) {
                val result = msg.obj as? MutableMap<String, Any>
                val resultStatus = result.stringValue("resultStatus")
                when (resultStatus) {
                    "9000" -> listener?.onFail("订单支付成功")
                    else -> listener?.onFail("支付失败")
                }
            }
        }
    }

    override fun doPay(
        type: OtherType,
        orderInfo: String,
        listener: IOtherListener
    ) {
        this.listener = listener
        val payRunnable = Runnable {
            val aliPay = PayTask(AppActivityManager.topActivity)
            val result = aliPay.payV2(orderInfo, true)

            val msg = Message()
            msg.what = 0x999
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        val payThread = Thread(payRunnable)
        payThread.start()
    }
}