package com.leeeyou.kotlinmvp.factory;

import com.leeeyou.kotlinmvp.presenter.BaseMvpPresenter;
import com.leeeyou.kotlinmvp.view.IMvpBaseView;

import org.jetbrains.annotations.NotNull;

/**
 * ClassName:   PresenterFactoryImpl
 * Description: Java Edition, PresenterFactory implementation class for creating Presenter objects
 * <p>
 * Author:      leeeyou
 * Date:        2017/12/28 16:46
 */
public class PresenterFactoryImpl2<V extends IMvpBaseView, P extends BaseMvpPresenter<V>> implements PresenterFactory<V, P> {

    private final Class<P> mPresenterClass;

    private PresenterFactoryImpl2(Class<P> presenterClass) {
        this.mPresenterClass = presenterClass;
    }

    public static <V extends IMvpBaseView, P extends BaseMvpPresenter<V>> PresenterFactoryImpl2<V, P> createFactory(Class<?> viewClazz) {
        CreatePresenter annotation = viewClazz.getAnnotation(CreatePresenter.class);
        Class<P> aClass = null;
        if (annotation != null) {
            aClass = (Class<P>) annotation.value();
        }
        return aClass == null ? null : new PresenterFactoryImpl2(aClass);
    }

    @NotNull
    @Override
    public P createPresenter() {
        try {
            return mPresenterClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Presenter creation failed, check if @CreatePresenter (xx.class) annotation is declared");
        }
    }
}
