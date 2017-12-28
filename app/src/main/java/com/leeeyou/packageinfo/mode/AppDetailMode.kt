package com.leeeyou.packageinfo.view.mode

import android.text.TextUtils
import com.leeeyou.packageinfo.bean.AppInfo
import java.text.DateFormat
import java.util.*


/**
 * ClassName:   AppDetailMode                        
 * Description: MVP : Mode for AppDetailActivity
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 17:04                     
 */
class AppDetailMode {

    internal fun transformData(appInfo: AppInfo?, appDetailCallback: Callback) {
        if (appInfo == null) {
            appDetailCallback.onFail()
            return
        }

        val dataPair: ArrayList<Pair<String, String>> = arrayListOf()
        dataPair.add(Pair("packageName", appInfo.packageName!!))

        if (TextUtils.isEmpty(appInfo.launcherActivity)) {
            dataPair.add(Pair("launcherActivity", "None"))
        } else {
            dataPair.add(Pair("launcherActivity", appInfo.launcherActivity!!))
        }

        dataPair.add(Pair("versionName", appInfo.versionName!!))
        dataPair.add(Pair("versionCode", appInfo.versionCode.toString()))
        dataPair.add(Pair("installDate", DateFormat.getDateInstance().format(Date(appInfo.installDate!!.toLong()))))
        dataPair.add(Pair("signMD5", appInfo.signMD5!!))
        dataPair.add(Pair("size", appInfo.size!!.toString()))
        dataPair.add(Pair("permissionCount", appInfo.permissionCount.toString()))

        appDetailCallback.onSuccess(dataPair)
    }

    internal interface Callback {
        fun onSuccess(dataPair: ArrayList<Pair<String, String>>)
        fun onFail()
    }

}