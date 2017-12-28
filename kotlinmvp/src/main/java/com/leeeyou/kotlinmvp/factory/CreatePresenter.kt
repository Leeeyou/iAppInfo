package com.leeeyou.kotlinmvp.factory

import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter

import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.reflect.KClass


/**
 * ClassName:   CreatePresenter
 * Description: Annotation class, used to create Presenter
 *
 * Author:      leeeyou
 * Date:        2017/12/28 16:38
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
annotation class CreatePresenter(val value: KClass<out BaseMvpPresenter<*>>)
