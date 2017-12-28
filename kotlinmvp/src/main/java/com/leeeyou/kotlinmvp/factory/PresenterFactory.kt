package com.leeeyou.kotlinmvp.factory

import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter
import com.leeeyou.kotlinmvp.view.IMvpBaseView

/**
 * ClassName:   PresenterFactory
 * Description: Presenter factory class
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 16:45                     
 */
interface PresenterFactory<V : IMvpBaseView, out P : BaseMvpPresenter<V>> {
    fun createPresenter(): P
}