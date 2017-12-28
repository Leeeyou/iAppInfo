package com.leeeyou.kotlinmvp.proxy

import com.leeeyou.kotlinmvp.factory.PresenterFactory
import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.kotlinmvp.view.IMvpBaseView

/**
 * ClassName:   PresenterProxyInterface                        
 * Description: Presenter proxy class interface, provides a way to maintain the factory
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 16:56                     
 */
interface PresenterProxyInterface<V : IMvpBaseView, P : BaseMvpPresenter<V>> {
    fun setPresenterFactory(factory: PresenterFactory<V, P>?)
    fun getPresenterFactory(): PresenterFactory<V, P>?
    fun getPresenter(): P?
}