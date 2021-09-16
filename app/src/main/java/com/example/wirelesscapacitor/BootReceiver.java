package com.example.wirelesscapacitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//            MyErrorLog.e("自启动了 ！！！！！","Success");
//            Intent newIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
//            //1.如果自启动APP，参数为需要自动启动的应用包名
//            Intent intent_1 = context.getPackageManager().getLaunchIntentForPackage("com.example.wirelesscapacitor");
//            //这句话必须加上才能开机自动运行app的界面
//            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //2.如果自启动Activity
//            context.startActivity(newIntent);
//            //3.如果自启动服务
//            context.startService(intent_1);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}