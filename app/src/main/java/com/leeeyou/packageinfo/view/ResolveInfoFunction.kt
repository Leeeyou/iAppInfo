package com.leeeyou.packageinfo.view

import android.content.pm.ResolveInfo
import android.util.Log
import com.leeeyou.packageinfo.bean.AppInfo
import io.reactivex.functions.Function

/**
 * Created by DELL on 2017/9/26.
 */
class ResolveInfoFunction<T> : Function<T, MutableList<AppInfo>> {
    override fun apply(t: T): MutableList<AppInfo> {
        val resolveInfo = t as MutableList<ResolveInfo>
        Log.e("zzz", resolveInfo.toString())
        return arrayListOf()
    }

}