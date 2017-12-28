package com.leeeyou.packageinfo.show

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.leeeyou.kotlinmvp.factory.CreatePresenter
import com.leeeyou.kotlinmvp.view.AbstractMvpActivity
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.util.copyToClipboard
import com.leeeyou.packageinfo.util.openApp
import com.leeeyou.packageinfo.util.toast
import com.leeeyou.packageinfo.view.AppDetailView
import com.leeeyou.packageinfo.view.adapter.AppDetailInfoAdapter
import com.leeeyou.packageinfo.view.presenter.AppDetailPresenter
import kotlinx.android.synthetic.main.activity_app_detail.*
import java.io.File

/**
 * ClassName:   AppDetailMVPActivity                        
 * Description: MVP Edition, App detail info activity
 * 
 * Author:      leeeyou                             
 * Date:        2017/12/28 17:10                     
 */
@CreatePresenter(AppDetailPresenter::class)
class AppDetailMVPActivity : AbstractMvpActivity<AppDetailView, AppDetailPresenter>(), AppDetailView {

    private lateinit var appInfo: AppInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        appInfo = intent.getParcelableExtra("appInfo")

        renderHeadView(appInfo)
//        setPresenterFactory(PresenterFactoryImpl(AppDetailPresenter::class.java))
    }

    override fun onResume() {
        super.onResume()
        getPresenter()!!.processAppDetailData(appInfo)
    }

    override fun renderHeadView(appInfo: AppInfo) {
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbarLayout.title = appInfo.appName

        if (File(appInfo.iconUrl).exists()) {
            val p = Palette.from(BitmapFactory.decodeFile(appInfo.iconUrl)).generate()
            val dominantSwatch = p.dominantSwatch
            if (dominantSwatch != null) {
                collapsingToolbarLayout.setBackgroundColor(Color.argb(180, Color.red(dominantSwatch.rgb), Color.green(dominantSwatch.rgb), Color.blue(dominantSwatch.rgb)))
                collapsingToolbarLayout.contentScrim = ColorDrawable(dominantSwatch.rgb)
                StatusBarUtil.setColor(this, dominantSwatch.rgb, 140)
            }

            imgIcon.setImageURI(Uri.fromFile(File(appInfo.iconUrl)))
            imgIcon.setOnClickListener({
                super.onBackPressed()
            })
        }
    }

    override fun renderRecyclerView(data: ArrayList<Pair<String, String>>) {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val appDetailInfoAdapter = AppDetailInfoAdapter(R.layout.item_detail_info, data)
        recyclerView.adapter = appDetailInfoAdapter

        appDetailInfoAdapter.setOnItemLongClickListener { adapter, _, position ->
            val pair = adapter.getItem(position) as Pair<*, *>

            copyToClipboard(pair.second.toString())
            toast(R.string.clipSuccess2)
            true
        }
    }

    override fun parseDataFail() {
        toast("parse data fail")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.open -> {
            openApp(appInfo.packageName!!, appInfo.launcherActivity!!)
            true
        }
        R.id.clip -> {
            getPresenter()!!.clipAppDetailInfo()
            true
        }
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun clipAppDetailInfo(dataPair: ArrayList<Pair<String, String>>) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("appName").append(" : ").append(appInfo.appName).append("\n\n")
        dataPair.forEach { stringBuilder.append(it.first).append(" : ").append(it.second).append("\n\n") }

        copyToClipboard(stringBuilder.toString())
        toast(R.string.clipSuccess2)
    }

}