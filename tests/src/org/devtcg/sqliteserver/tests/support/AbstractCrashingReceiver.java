package org.devtcg.sqliteserver.tests.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;
import org.devtcg.sqliteserver.tests.util.CrashHelper;
import org.devtcg.sqliteserver.tests.util.ProcessHelper;

abstract class AbstractCrashingReceiver extends BroadcastReceiver {
    private static final String TAG = AbstractCrashingReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String targetProcessName = intent.getStringExtra(CrashHelper.EXTRA_TARGET_PROCESS_NAME);
        String currentProcessName = ProcessHelper.getProcessName(context);
        if (currentProcessName.equals(targetProcessName)) {
            String callerName = ProcessHelper.getProcessName(context, Binder.getCallingPid());
            Log.i(TAG, "Crash requested by " + callerName);
            System.exit(1);
        }
    }
}
