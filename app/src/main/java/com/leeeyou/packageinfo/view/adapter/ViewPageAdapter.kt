package com.leeeyou.packageinfo.view.adapter

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.Toolbar
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.view.AppInfoFragment

/**
 * Created by leeeyou on 2017/9/21.
 *
 * viewPage adapter, and save two variables, tabLayout and toolbar
 */
class ViewPageAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private var dataList: MutableList<AppInfo>? = null
    private var tabLayout: TabLayout? = null
    private var toolbar: Toolbar? = null

    constructor(fm: FragmentManager?, data: MutableList<AppInfo>, tabLayout: TabLayout, toolbar: Toolbar) : this(fm) {
        dataList = data
        this.tabLayout = tabLayout
        this.toolbar = toolbar
    }

    override fun getItem(position: Int): Fragment =
            AppInfoFragment.newInstance(dataList, tabLayout, toolbar, position)

    override fun getCount(): Int = 2
}