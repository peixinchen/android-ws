package com.peixinchen.mion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by peixi on 2017/11/18.
 */

class WechatShareUtil {
    static public void share(Context context, String tagline, List<String> filenameList) {
        Log.d("API", tagline);
        Log.d("API", filenameList.toString());

        if (!isWechatInstalled(context)) {
            Log.d("PXC", "没有安装微信");
            return;
        } else {
            Log.d("PXC", "安装微信");
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");

        ArrayList<Uri> imageList = new ArrayList<>();
        for (String filename: filenameList) {
            File file = new File(filename);
            imageList.add(Uri.fromFile(file));
        }

        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageList);
        intent.putExtra("Kdescription", tagline);
        context.startActivity(intent);
    }

    protected static boolean isWechatInstalled(Context context) {
        PackageInfo packageInfo = null;

        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);

        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
