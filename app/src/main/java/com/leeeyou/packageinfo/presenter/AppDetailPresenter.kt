package com.leeeyou.packageinfo.view.presenter

import android.os.Bundle
import android.util.Log
import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.view.AppDetailView
import com.leeeyou.packageinfo.view.mode.AppDetailMode
import java.util.*

/**
 * ClassName:   AppDetailPresenter                        
 * Description: MVP : Presenter for AppDetailActivity
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 17:05                     
 */
class AppDetailPresenter : BaseMvpPresenter<AppDetailView>() {

    private var mAppDetailMode: AppDetailMode = AppDetailMode()
    lateinit var mDataPair: ArrayList<Pair<String, String>>

    private val mTAG: String = "MVP:AppDetailPresenter"

    override fun onCreatePresenter(savedState: Bundle?) {
        super.onCreatePresenter(savedState)
        if (savedState != null) {
            Log.e(mTAG, "onCreatePresenter")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e(mTAG, "onSaveInstanceState")
        outState.putString("AppDetailPresenter", "appDetailPresenter")
    }

    override fun onDestroyPresenter() {
        super.onDestroyPresenter()
        Log.e(mTAG, "onDestroyPresenter")
    }

    fun processAppDetailData(appInfo: AppInfo) {
        mAppDetailMode!!.transformData(appInfo, object : AppDetailMode.Callback {
            override fun onFail() {
                getMvpView()!!.parseDataFail()
            }

            override fun onSuccess(dataPair: ArrayList<Pair<String, String>>) {
                mDataPair = dataPair
                getMvpView()!!.renderRecyclerView(dataPair)
            }
        })
    }

    fun clipAppDetailInfo() {
        getMvpView()!!.clipAppDetailInfo(mDataPair)
    }


}