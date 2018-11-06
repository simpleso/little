package top.andnux.little.core.utils

import java.lang.Exception
import java.util.concurrent.*

object ThreadManager {

    private var threadPoolExecutor: ThreadPoolExecutor? = null
    private var mQue = LinkedBlockingQueue<Runnable>();

    private fun init(): Unit {
        threadPoolExecutor = ThreadPoolExecutor(
            4,
            20,
            1,
            TimeUnit.MINUTES,
            ArrayBlockingQueue<Runnable>(20),
            Executors.defaultThreadFactory(),
            this.handler
        )
        threadPoolExecutor?.execute(workRunnable)
    }

    private var workRunnable = Runnable {
        while (true) {
           try {
               val runnable = mQue.take()
               threadPoolExecutor?.execute(runnable)
           }catch (e:Exception){
               e.printStackTrace()
           }
        }
    }

    fun execute(runnable: Runnable): Unit {
        if (threadPoolExecutor == null) {
            init()
        }
        mQue.put(runnable)
    }

    private var handler = RejectedExecutionHandler { r, _ ->
        run {
            try {
                mQue.put(r)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}