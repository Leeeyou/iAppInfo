package com.leeeyou.packageinfo.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import kotlinx.android.synthetic.main.activity_app_detail.*

class AppDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        val appInfo = intent.getParcelableExtra<AppInfo>("appInfo")
        val position = intent.getIntExtra("position", 0)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imgIcon.transitionName = "image" + position
//        }

        imgIcon.setImageBitmap(appInfo.icon)
        imgIcon.setOnClickListener(View.OnClickListener {
            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    android.support.v4.util.Pair<View, String>(imgIcon, getString(R.string.view_name_image_icon)))

            val intent = Intent(this, MainActivity().javaClass)

            ActivityCompat.startActivity(this, intent, activityOptions.toBundle())
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.finishAfterTransition(this)
        }


    }
}
