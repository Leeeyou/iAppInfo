package com.leeeyou.kotlinmvp.view

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.leeeyou.kotlinmvp.factory.PresenterFactory
import com.leeeyou.kotlinmvp.factory.PresenterFactoryImpl2
import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.kotlinmvp.proxy.BaseMvpProxy
import com.leeeyou.kotlinmvp.proxy.PresenterProxyInterface

/**
 * ClassName:   AbstractMvpActivity
 * Description: MvpActivity base class
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 17:01                     
 */
abstract class AbstractMvpActivity<V : IMvpBaseView, P : BaseMvpPresenter<V>> : AppCompatActivity(), PresenterProxyInterface<V, P> {
    private val mPresenterSaveKey = "presenter_save_key"

    private var mProxy = BaseMvpProxy<V, P>(PresenterFactoryImpl2.createFactory(javaClass))

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        if (savedInstanceState != null) {
            mProxy.onRestoreInstanceState(savedInstanceState.getBundle(mPresenterSaveKey))
        }
    }

    override fun onResume() {
        super.onResume()
        mProxy.onResume(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mProxy.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putBundle(mPresenterSaveKey, mProxy.onSaveInstanceState())
    }

    override fun setPresenterFactory(factory: PresenterFactory<V, P>?) {
        mProxy.setPresenterFactory(factory)
    }

    override fun getPresenterFactory(): PresenterFactory<V, P>? = mProxy.getPresenterFactory()

    override fun getPresenter(): P? = mProxy.getPresenter()
}