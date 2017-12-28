package com.leeeyou.kotlinmvp.presenter

import android.os.Bundle
import android.util.Log
import com.leeeyou.kotlinmvp.view.IMvpBaseView

/**
 * ClassName:   BaseMvpPresenter                        
 * Description: Presenter base class
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 16:54                     
 */
open class BaseMvpPresenter<V : IMvpBaseView> {
    private val mTag: String = "BaseMvpPresenter"

    private var mMvpView: V? = null

    open fun onCreatePresenter(savedState: Bundle?) {
        Log.e(mTag, "P onCreatePresenter = ")
    }

    open fun onAttachMvpView(mvpView: V) {
        mMvpView = mvpView
    }

    open fun onDetachMvpView() {
        mMvpView = null
    }

    open fun onDestroyPresenter() {
        Log.e(mTag, "P onDestroyPresenter = ")
    }

    open fun onSaveInstanceState(outState: Bundle) {
        Log.e(mTag, "P onSaveInstanceState = ")
    }

    fun getMvpView(): V? = mMvpView
}