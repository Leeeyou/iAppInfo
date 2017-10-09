package com.leeeyou.packageinfo.view

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.leeeyou.packageinfo.R
import com.leeeyou.packageinfo.bean.AppInfo
import com.leeeyou.packageinfo.drawableToBitmap
import com.leeeyou.packageinfo.openGithub
import com.leeeyou.packageinfo.view.adapter.ViewPageAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by leeeyou on 2017/9/21.
 *
 * main page
 */
class MainActivity : AppCompatActivity() {

    private var appInfoList: MutableList<AppInfo> = arrayListOf()
    private val mAppDir = File(Environment.getExternalStorageDirectory(), "AppInfo")
    private var mMaterialDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        tabLayout.addTab(tabLayout.newTab().setText("SystemApp"), 0)
        tabLayout.addTab(tabLayout.newTab().setText("UserApp"), 1)
        tabLayout.setupWithViewPager(viewpager)

        loadApps()
    }

    private fun loadApps() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        Observable.fromArray(packageManager.queryIntentActivities(intent, 0))
                .map {
                    it.forEach {
                        val appInfo = AppInfo()

                        appInfo.packageName = it.activityInfo.packageName
                        appInfo.launcherActivity = it.activityInfo.name
                        appInfo.appName = it.activityInfo.loadLabel(packageManager) as String?
                        appInfo.iconUrl = saveBitmapToLocal(appInfo.packageName, drawableToBitmap(it.activityInfo.loadIcon(packageManager)))

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
                    return@map appInfoList
                }
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(
                        {
                            mMaterialDialog = MaterialDialog.Builder(this)
                                    .content("正在解析应用...")
                                    .progress(true, 0)
                                    .progressIndeterminateStyle(true)
                                    .show()
                        }

                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {
                            mMaterialDialog!!.dismiss()
                            Log.e("onError", it.printStackTrace().toString())
                        },
                        onComplete = {
                            viewpager.adapter = ViewPageAdapter(supportFragmentManager, appInfoList, tabLayout, toolbar)
                            tabLayout.getTabAt(0)!!.text = "SystemApp"
                            tabLayout.getTabAt(1)!!.text = "UserApp"
                            mMaterialDialog!!.dismiss()
                        }
                )

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
            openGithub("https://github.com/LeeeYou/AppInfo")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun saveBitmapToLocal(packageName: String?, bmp: Bitmap): String {
        if (!mAppDir.exists()) {
            mAppDir.mkdir()
        }

        val file = File(mAppDir, packageName + ".png")
        if (file.exists()) {
            return file.absolutePath
        }

        try {
            val fos = BufferedOutputStream(FileOutputStream(file))
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

}

