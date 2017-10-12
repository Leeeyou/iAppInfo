package com.leeeyou.packageinfo.view

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.copyToClipboard
import com.leeeyou.packageinfo.openApp
import com.leeeyou.packageinfo.toast
import com.leeeyou.packageinfo.view.adapter.AppDetailInfoAdapter
import kotlinx.android.synthetic.main.activity_app_detail.*
import java.io.File
import java.text.DateFormat
import java.util.*


class AppDetailActivity : AppCompatActivity() {

    private lateinit var appInfo: AppInfo
    private lateinit var dataPair: ArrayList<Pair<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        appInfo = intent.getParcelableExtra("appInfo")

        setHeadViewStyle(appInfo)
        transformData(appInfo)
        initRecyclerView(dataPair)
    }

    private fun initRecyclerView(data: ArrayList<Pair<String, String>>) {
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

    private fun transformData(appInfo: AppInfo) {
        dataPair = arrayListOf()
        dataPair.add(Pair(getString(R.string.packageName), appInfo.packageName!!))

        if (TextUtils.isEmpty(appInfo.launcherActivity)) {
            dataPair.add(Pair(getString(R.string.launcherActivity), "None"))
        } else {
            dataPair.add(Pair(getString(R.string.launcherActivity), appInfo.launcherActivity!!))
        }

        dataPair.add(Pair(getString(R.string.versionName), appInfo.versionName!!))
        dataPair.add(Pair(getString(R.string.versionCode), appInfo.versionCode.toString()))
        dataPair.add(Pair(getString(R.string.installDate), DateFormat.getDateInstance().format(Date(appInfo.installDate!!.toLong()))))
        dataPair.add(Pair(getString(R.string.signMD5), appInfo.signMD5!!))
        dataPair.add(Pair(getString(R.string.size), Formatter.formatFileSize(this, appInfo.size!!.toLong())))
        dataPair.add(Pair(getString(R.string.permissionCount), appInfo.permissionCount.toString()))
    }

    private fun setHeadViewStyle(appInfo: AppInfo) {
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
            clipAppInfo()
            true
        }
        android.R.id.home -> {
            super.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun clipAppInfo() {
        val stringBuilder = StringBuilder()
        stringBuilder.append("appName").append("\n").append(appInfo.appName).append("\n")
        dataPair.forEach { stringBuilder.append(it.first).append("\n").append(it.second).append("\n") }

        copyToClipboard(stringBuilder.toString())
        toast(R.string.clipSuccess2)
    }


}

