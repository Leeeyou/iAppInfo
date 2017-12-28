package com.leeeyou.packageinfo.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.leeeyou.packageinfo.R

/**
 * ClassName:   AppDetailInfoAdapter
 * Description: app detail info adapter
 *
 * Author:      leeeyou
 * Date:        2017/9/21 17:03
 */
class AppDetailInfoAdapter(layoutResId: Int, data: List<Pair<String, String>>) : BaseQuickAdapter<Pair<String, String>, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Pair<String, String>?) {
        helper!!.setText(R.id.desc, item!!.first)
                .setText(R.id.value, item.second)
    }

}