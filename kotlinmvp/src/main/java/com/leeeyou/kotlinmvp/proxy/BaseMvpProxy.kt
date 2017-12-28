package com.leeeyou.kotlinmvp.proxy

import android.os.Bundle
import com.leeeyou.kotlinmvp.factory.PresenterFactory
import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.kotlinmvp.view.IMvpBaseView


/**
 * ClassName:   BaseMvpProxy                        
 * Description: Maintain Presenter's lifecycle and factory methods
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 16:56                     
 */
class BaseMvpProxy<V : IMvpBaseView, P : BaseMvpPresenter<V>>(private var mFactory: PresenterFactory<V, P>?) : PresenterProxyInterface<V, P> {
    private val mPresenterKey: String = "presenter_key"

    private var mPresenter: P? = null
    private var mBundle: Bundle? = null
    private var mIsAttachView = false

    fun onResume(mvpView: V) {
        getPresenter()
        if (mPresenter != null && !mIsAttachView) {
            mPresenter!!.onAttachMvpView(mvpView)
            mIsAttachView = true
        }
    }

    fun onDetachMvpView() {
        if (mPresenter != null && mIsAttachView) {
            mPresenter!!.onDetachMvpView()
            mIsAttachView = false
        }
    }

    fun onDestroy() {
        if (mPresenter != null) {
            onDetachMvpView()
            mPresenter!!.onDestroyPresenter()
            mPresenter = null
        }
    }

    fun onSaveInstanceState(): Bundle {
        val bundle = Bundle()
        getPresenter()
        if (mPresenter != null) {
            val presenterBundle = Bundle()
            mPresenter!!.onSaveInstanceState(presenterBundle)
            bundle.putBundle(mPresenterKey, presenterBundle)
        }
        return bundle
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mBundle = savedInstanceState
    }

    override fun setPresenterFactory(factory: PresenterFactory<V, P>?) {
        if (mPresenter != null) {
            throw IllegalArgumentException("It is not allowed to create Presenter object repeatedly")
        }
        mFactory = factory
    }

    override fun getPresenterFactory(): PresenterFactory<V, P>? = mFactory

    override fun getPresenter(): P? {
        if (mFactory != null && mPresenter == null) {
            mPresenter = mFactory!!.createPresenter()
            mPresenter!!.onCreatePresenter(if (mBundle == null) null else mBundle!!.getBundle(mPresenterKey))
        }
        return mPresenter
    }
}