package com.leeeyou.packageinfo.view.adapter

import android.graphics.BitmapFactory
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo


/**
 * Created by leeeyou on 2017/9/21.
 *
 * System app adapter
 */
class AppInfoAdapter(layoutResId: Int, data: List<AppInfo>) : BaseQuickAdapter<AppInfo, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: AppInfo?) {
        helper!!.setText(R.id.name, item!!.appName)
                .setImageBitmap(R.id.imgIcon, BitmapFactory.decodeFile(item.iconUrl))
                .setText(R.id.activity, item.packageName)
    }


}