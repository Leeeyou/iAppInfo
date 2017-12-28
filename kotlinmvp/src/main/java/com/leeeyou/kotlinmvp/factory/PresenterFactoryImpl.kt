package com.leeeyou.kotlinmvp.factory

import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.kotlinmvp.view.IMvpBaseView

/**
 * ClassName:   PresenterFactoryImpl
 * Description: Kotlin Edition, PresenterFactory implementation class for creating Presenter objects
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 16:46                     
 */
class PresenterFactoryImpl<V : IMvpBaseView, out P : BaseMvpPresenter<V>>(private var mPresenterClass: Class<P>) : PresenterFactory<V, P> {

    override fun createPresenter(): P {
        try {
            return mPresenterClass!!.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Presenter creation failed, check if @CreatePresenter (xx.class) annotation is declared")
        }
    }

    companion object {
        fun <V : IMvpBaseView, P : BaseMvpPresenter<V>> createFactory(viewClazz: Class<*>): PresenterFactoryImpl<V, P>? {
            val annotation = viewClazz.getAnnotation(CreatePresenter::class.java)
            var aClass: Class<P>? = null
            if (annotation != null) {
                aClass = annotation.value as Class<P>
            }
            return if (aClass == null) null else PresenterFactoryImpl(aClass)
        }
    }
}