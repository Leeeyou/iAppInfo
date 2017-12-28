package com.leeeyou.packageinfo.view.adapter

import android.graphics.BitmapFactory
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo

/**
 * ClassName:   AppInfoAdapter
 * Description: System app adapter
 * 
 * Author:      leeeyou                             
 * Date:        2017/09/21 17:04
 */
class AppInfoAdapter(layoutResId: Int, data: List<AppInfo>) : BaseQuickAdapter<AppInfo, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: AppInfo?) {
        helper!!.setText(R.id.name, item!!.appName)
                .setImageBitmap(R.id.imgIcon, BitmapFactory.decodeFile(item.iconUrl))
                .setText(R.id.activity, item.packageName)
    }


}