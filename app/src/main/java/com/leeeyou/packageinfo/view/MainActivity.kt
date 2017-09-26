package com.leeeyou.packageinfo.view

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.view.adapter.ViewPageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by leeeyou on 2017/9/21.
 *
 * main page
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        tabLayout.setupWithViewPager(viewpager)
        viewpager.adapter = ViewPageAdapter(supportFragmentManager, loadApps(), tabLayout, toolbar)
        tabLayout.addTab(tabLayout.newTab().setText("SystemApp"), 0)
        tabLayout.addTab(tabLayout.newTab().setText("UserApp"), 1)
    }

    private fun loadApps(): MutableList<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val apps: MutableList<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
        val appInfoList: MutableList<AppInfo> = arrayListOf()

        for (i in 0 until apps.size) {
            val info = apps[i]

            val appInfo = AppInfo()

            appInfo.packageName = info.activityInfo.packageName
            appInfo.launcherActivity = info.activityInfo.name
            appInfo.appName = info.activityInfo.loadLabel(packageManager) as String?
            appInfo.icon = drawableToBitmap(info.activityInfo.loadIcon(packageManager))

            val applicationInfo = packageManager.getApplicationInfo(appInfo.packageName, 0)
            appInfo.isSystemApp = applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) != 0

            val packageInfo = packageManager.getPackageInfo(appInfo.packageName, PackageManager.GET_PERMISSIONS)
            appInfo.versionCode = packageInfo.versionCode
            appInfo.versionName = packageInfo.versionName

            appInfo.installDate = packageInfo.firstInstallTime
            appInfo.permissionCount = packageInfo.requestedPermissions?.size

            appInfo.signMD5 = getSignMd5Str(appInfo.packageName)
            appInfo.size = getApkSize(appInfo.packageName)

            appInfoList.add(appInfo)
        }

        return appInfoList
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

    /**
     * MD5加密
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    private fun encryptionMD5(byteStr: ByteArray): String {
        val messageDigest: MessageDigest?
        val md5StrBuff = StringBuffer()
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            messageDigest!!.reset()
            messageDigest.update(byteStr)
            val byteArray = messageDigest.digest()
            for (i in byteArray.indices) {
                if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF and byteArray[i].toInt()))
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return md5StrBuff.toString()
    }

    /**
     * 获取app签名md5值
     */
    private fun getSignMd5Str(packageName: String?): String {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val signs = packageInfo.signatures
            val sign = signs[0]
            return encryptionMD5(sign.toByteArray())
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    @Throws(PackageManager.NameNotFoundException::class)
    private fun getApkSize(packageName: String?): Long =
            File(packageManager.getApplicationInfo(packageName, 0).publicSourceDir).length()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.github -> {
            openGithub()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openGithub() {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        intent.data = Uri.parse("https://github.com/LeeeYou/AppInfo")
        startActivity(intent)

    }
}
