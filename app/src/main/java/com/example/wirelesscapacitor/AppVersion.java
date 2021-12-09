package com.example.wirelesscapacitor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppVersion {
    public static  AppVersion instance = null;

    public AppVersion() {
        AppVersion.instance = this;
    }
    public int getAppVersionName(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versioncode <=0) {
                return 0;
            }
//            MyLog.e("Version", "VersionName:" + versionName + "    VersionCode:" + versioncode);
        } catch (Exception e) {
//            MyLog.e("VersionInfo", "Exception" + e);
        }
        if (versionName != null || versionName != "") {
        } else {
//            MyLog.e("Version is Null", "Get TalkDoo Version Error");
        }
        return versioncode;
    }
}
