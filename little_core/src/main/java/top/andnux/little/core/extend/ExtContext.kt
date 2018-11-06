package top.andnux.little.core.extend

import android.content.Context
import android.content.pm.PackageInfo

val Context.packageInfo: PackageInfo
    get() {
        return packageManager.getPackageInfo(packageName, 0)
    }

val Context.versionName: String
    get() {
        return packageInfo.versionName
    }

val Context.versionCode: Int
    get() {
        return packageInfo.versionCode
    }
