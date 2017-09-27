package com.leeeyou.packageinfo

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.ClipboardManager
import android.widget.Toast

/**
 * Created by leeeyou on 2017/9/26.
 *
 *
 */
fun Activity.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Activity.toast(stringId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(stringId), duration).show()
}

fun Activity.copyToClipboard(str: String) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).text = str
}

fun Activity.openApp(packageName: String, launcherActivityName: String) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.component = ComponentName(packageName, launcherActivityName)
    startActivity(intent)
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}

fun Activity.openGithub(url: String) {
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    intent.data = Uri.parse(url)
    startActivity(intent)
}
