package com.leeeyou.packageinfo.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by leeeyou on 2017/9/22.
 *
 *
 */
data class AppInfo(var packageName: String? = null,
                   var launcherActivity: String? = "",
                   var appName: String? = null,
                   var iconUrl: String? = null,
                   var isSystemApp: Boolean? = null,
                   var versionCode: Int? = 0,
                   var versionName: String? = null,
                   var installDate: Long? = null,
                   var signMD5: String? = null,
                   var signSHA: String? = null,
                   var size: Long? = 0,
                   var permissionCount: Int? = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(packageName)
        parcel.writeString(launcherActivity)
        parcel.writeString(appName)
        parcel.writeString(iconUrl)
        parcel.writeValue(isSystemApp)
        parcel.writeValue(versionCode)
        parcel.writeString(versionName)
        parcel.writeValue(installDate)
        parcel.writeString(signMD5)
        parcel.writeString(signSHA)
        parcel.writeValue(size)
        parcel.writeValue(permissionCount)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AppInfo> {
        override fun createFromParcel(parcel: Parcel): AppInfo = AppInfo(parcel)

        override fun newArray(size: Int): Array<AppInfo?> = arrayOfNulls(size)
    }


}