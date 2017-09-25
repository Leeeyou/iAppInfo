package com.leeeyou.packageinfo.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.jaeger.library.StatusBarUtil
import com.leeeyou.packageinfo.Constant
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.view.adapter.AppInfoAdapter
import kotlinx.android.synthetic.main.fragment_system_app.*


/**
 * Created by leeeyou on 2017/9/21.
 *
 * the app info list fragment
 */
class AppInfoFragment() : Fragment() {
    private lateinit var mList: MutableList<AppInfo>
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private var witchPosition: Int = 0

    constructor(mList: MutableList<AppInfo>, tabLayout: TabLayout, toolbar: Toolbar, witchPosition: Int) : this() {
        this.mList = mList
        this.tabLayout = tabLayout
        this.toolbar = toolbar
        this.witchPosition = witchPosition
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_system_app, null)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val predicate: (AppInfo) -> Boolean = {
            if (witchPosition == Constant.IS_SYSTEM_APP) {
                it.isSystemApp!!
            } else {
                !it.isSystemApp!!
            }
        }

        val systemAppAdapter = AppInfoAdapter(R.layout.item_packageinfo, mList.filter(predicate))
        recyclerView.adapter = systemAppAdapter

        systemAppAdapter.setOnItemClickListener { adapter, _, position ->
            val appInfo: AppInfo = adapter.getItem(position) as AppInfo
            val drawableToBitmap = drawableToBitmap(appInfo.icon!!)
            Palette.from(drawableToBitmap).generate {
                val dominantSwatch = it.dominantSwatch
                if (dominantSwatch != null) {
                    tabLayout.setBackgroundColor(dominantSwatch.rgb)
                    toolbar.setBackgroundColor(dominantSwatch.rgb)
                    StatusBarUtil.setColor(activity, dominantSwatch.rgb, 80)
                }

                val lightVibrantSwatch = it.lightVibrantSwatch
                if (lightVibrantSwatch != null) {
                    val darkColor = lightVibrantSwatch.bodyTextColor

                    val selectColor = Color.rgb(Math.abs(Color.red(darkColor) - 255), Math.abs(Color.green(darkColor) - 255), Math.abs(Color.blue(darkColor) - 255))
                    val normalColor = Color.argb(126, Math.abs(Color.red(darkColor) - 255), Math.abs(Color.green(darkColor) - 255), Math.abs(Color.blue(darkColor) - 255))

                    tabLayout.setTabTextColors(normalColor, selectColor)
                    tabLayout.setSelectedTabIndicatorColor(selectColor)
                    toolbar.setTitleTextColor(selectColor)
                }

                startActivity(Intent(context, AppDetailActivity().javaClass))
            }
        }

        systemAppAdapter.setOnItemLongClickListener { adapter, _, position ->
            val appInfo: AppInfo = adapter.getItem(position) as AppInfo

            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = appInfo.packageName
            Toast.makeText(context, R.string.clipSuccess, Toast.LENGTH_SHORT).show()
            true
        }

    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
}