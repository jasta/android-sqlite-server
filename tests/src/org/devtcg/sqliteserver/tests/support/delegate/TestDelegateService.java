package org.devtcg.sqliteserver.tests.support.delegate;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.tests.support.delegate.command.Command;
import org.devtcg.sqliteserver.tests.support.delegate.command.WriteStateFile;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Special client process that can be instructed to carry out {@link SQLiteServerConnection}
 * commands to test multi-client behaviour.
 * <p>
 * By we using IntentService we inherit a pretty serious gotcha whereby if we allow
 * the handler thread to shutdown we will spoil any test that was being executed.  To
 * prevent this in practice, we hook into onDestroy and make sure it is never called (this service
 * dies by force, never a graceful shutdown).
 */
public class TestDelegateService extends IntentService {
    private static final String TAG = TestDelegateService.class.getSimpleName();

    private static final String EXTRA_COMMAND = "command";

    private DelegateState mState;

    public TestDelegateService() {
        super(TAG);
        setIntentRedelivery(false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mState = new DelegateState();
        mState.context = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Command command = intent.getParcelableExtra(EXTRA_COMMAND);
        command.execute(mState);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Send commands in a serial fashion.
     *
     * @param context
     * @param command
     */
    public static void sendCommand(Context context, Command command) {
        Intent intent = new Intent(context, TestDelegateService.class);
        intent.putExtra(EXTRA_COMMAND, command);
        context.startService(intent);
    }

    /**
     * Block the caller until the server has processed all outstanding command messages.
     *
     * @param context
     * @return True if the state was synchronized; false if the timeout elapsed.
     * @throws IOException If the state file used for synchronization could not be deleted.
     */
    public static boolean synchronizeState(Context context, long waittime, TimeUnit unit)
            throws IOException {
        File tmpFile = makeTemporaryFilename("test-delegate", ".lock", context.getCacheDir());
        sendCommand(context, new WriteStateFile(tmpFile));
        return waitForFile(tmpFile, waittime, unit);
    }

    /**
     * Create a temporary filename, but don't create the file itself.
     *
     * @see File#createTempFile(String, String, java.io.File)
     */
    private static File makeTemporaryFilename(String prefix, String suffix, File dir) {
        try {
            File tmpFile = File.createTempFile(prefix, suffix, dir);

            // We just want the filename, its important that it does not yet exist.
            if (!tmpFile.delete()) {
                throw new IOException("Failed to delete temp file: " + tmpFile);
            }

            return tmpFile;
        } catch (IOException e) {
            // TODO: This sucks.
            throw new RuntimeException(e);
        }
    }

    /**
     * Wait for the specified file to exist and delete it once it does.  This is used as an
     * extremely crude multiprocess synchronization device.
     *
     * @param tmpFile
     * @param waittime
     * @param unit
     * @return True if the file was detected and subsequently deleted; false if the waittime was
     *     reached.
     * @throws IOException If the file could not be deleted.
     */
    private static boolean waitForFile(File tmpFile, long waittime, TimeUnit unit)
            throws IOException {
        long waittimeMillis = unit.toMillis(waittime);
        long startTime = System.currentTimeMillis();
        while (!tmpFile.exists()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // Ignore...
            }
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed > waittimeMillis) {
                return false;
            }
        }
        if (!tmpFile.delete()) {
            throw new IOException("Failed to delete file: " + tmpFile);
        }
        return true;
    }
}
