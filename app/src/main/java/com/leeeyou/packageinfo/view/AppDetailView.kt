package com.leeeyou.packageinfo.view

import com.leeeyou.kotlinmvp.view.IMvpBaseView
import com.leeeyou.packageinfo.bean.AppInfo

/**
 * ClassName:   AppDetailView                        
 * Description: MVP : View for AppDetailActivity
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 17:13                     
 */
interface AppDetailView : IMvpBaseView {
    fun renderHeadView(appInfo: AppInfo)
    fun renderRecyclerView(data: ArrayList<Pair<String, String>>)
    fun parseDataFail()
    fun clipAppDetailInfo(dataPair: ArrayList<Pair<String, String>>)
}