package com.leeeyou.packageinfo.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.text.ClipboardManager
import android.text.format.Formatter
import android.view.View
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.view.adapter.AppDetailInfoAdapter
import kotlinx.android.synthetic.main.activity_app_detail.*
import java.text.DateFormat
import java.util.*


class AppDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        val appInfo = intent.getParcelableExtra<AppInfo>("appInfo")

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val p = Palette.from(appInfo.icon).generate()
        val dominantSwatch = p.dominantSwatch
        if (dominantSwatch != null) {
            collapsingToolbarLayout.setBackgroundColor(Color.argb(180, Color.red(dominantSwatch.rgb), Color.green(dominantSwatch.rgb), Color.blue(dominantSwatch.rgb)))
            collapsingToolbarLayout.contentScrim = ColorDrawable(dominantSwatch.rgb)
            StatusBarUtil.setColor(this, dominantSwatch.rgb, 140)
        }

        collapsingToolbarLayout.title = appInfo.appName

        imgIcon.setImageBitmap(appInfo.icon)

        imgIcon.setOnClickListener({
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    android.support.v4.util.Pair<View, String>(imgIcon, getString(R.string.view_name_image_icon)))

            val intent = Intent(this, MainActivity().javaClass)

            ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
        })

        val data = arrayListOf<Pair<String, String>>()

        data.add(Pair(getString(R.string.packageName), appInfo.packageName!!))
        data.add(Pair(getString(R.string.launcherActivity), appInfo.launcherActivity!!))
        data.add(Pair(getString(R.string.versionName), appInfo.versionName!!))
        data.add(Pair(getString(R.string.versionCode), appInfo.versionCode.toString()))
        data.add(Pair(getString(R.string.installDate), DateFormat.getDateInstance().format(Date(appInfo.installDate!!.toLong()))))
        data.add(Pair(getString(R.string.signMD5), appInfo.signMD5!!))
        data.add(Pair(getString(R.string.size), Formatter.formatFileSize(this, appInfo.size!!.toLong())))
        data.add(Pair(getString(R.string.permissionCount), appInfo.permissionCount.toString()))

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val appDetailInfoAdapter = AppDetailInfoAdapter(R.layout.item_detail_info, data)
        recyclerView.adapter = appDetailInfoAdapter

        appDetailInfoAdapter.setOnItemLongClickListener { adapter, _, position ->
            val pair = adapter.getItem(position) as Pair<*, *>

            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = pair.second.toString()
            Toast.makeText(this, R.string.clipSuccess2, Toast.LENGTH_SHORT).show()
            true
        }
    }

}
