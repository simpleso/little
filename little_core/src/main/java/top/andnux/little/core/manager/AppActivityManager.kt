package top.andnux.little.core.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList

object AppActivityManager : Application.ActivityLifecycleCallbacks {

    private var mActivity: MutableList<WeakReference<Activity>> = ArrayList()

    val topActivity: Activity?
        get() {
            return mActivity.last().get()
        }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {
        mActivity.forEachIndexed { index, weakReference ->
            if (weakReference.get() == activity) {
                mActivity.removeAt(index)
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (activity != null) {
            mActivity.add(WeakReference(activity))
        }
    }
}