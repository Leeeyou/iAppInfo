package com.leeeyou.packageinfo.bean

import android.graphics.drawable.Drawable

/**
 * Created by leeeyou on 2017/9/22.
 *
 *
 */
data class AppInfo(var packageName: String? = null,
                   var launcherActivity: String? = null,
                   var appName: String? = null,
                   var icon: Drawable? = null,
                   var logo: Drawable? = null,
                   var isSystemApp: Boolean? = null,
                   var versionCode: Int? = 0,
                   var versionName: String? = null,
                   var installDate: Long? = null,
                   var signMD5: String? = null,
                   var signSHA: String? = null,
                   var size: Long? = 0,
                   var permissionCount: Int? = 0)