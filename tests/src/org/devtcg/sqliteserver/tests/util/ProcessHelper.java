package org.devtcg.sqliteserver.tests.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import static android.app.ActivityManager.RunningAppProcessInfo;

public class ProcessHelper {
    private static final String TAG = ProcessHelper.class.getSimpleName();

    public static String getProcessName(Context context) {
        int myPid = Process.myPid();
        return getProcessName(context, myPid);
    }

    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == pid) {
                return info.processName;
            }
        }

        Log.w(TAG, "Unexpected failure resolving pid " + pid + " to name");
        return String.valueOf(pid);
    }

    public static boolean inMainProcess(Context context) {
        String currentProcess = getProcessName(context);
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 0);
            String appProcess = appInfo.processName;
            return currentProcess.equals(appProcess);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isProcessRunning(Context context, String targetProcess) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (targetProcess.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
