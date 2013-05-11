package org.devtcg.sqliteserver.tests.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.devtcg.sqliteserver.tests.util.ProcessHelper;

import static junit.framework.Assert.assertTrue;

public class CrashHelper {
    private static final String ACTION_CRASH = "org.devtcg.sqliteserver.tests.ACTION_CRASH";
    static final String EXTRA_TARGET_PROCESS_NAME = "target_process_name";

    public static void synchronouslyCrash(Context context, String targetProcess) {
        assertTrue("Process " + targetProcess + " must already be alive",
                ProcessHelper.isProcessRunning(context, targetProcess));

        Intent intent = new Intent(ACTION_CRASH);
        intent.putExtra(EXTRA_TARGET_PROCESS_NAME, targetProcess);
        context.sendOrderedBroadcast(intent, null);

        waitForProcessDeath(context, targetProcess);
    }

    private static void waitForProcessDeath(Context context, String targetProcess) {
        while (ProcessHelper.isProcessRunning(context, targetProcess)) {
            // Sigh, synchronicity is really, really hard in this test environment...
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Ignore...
            }
        }
    }
}
