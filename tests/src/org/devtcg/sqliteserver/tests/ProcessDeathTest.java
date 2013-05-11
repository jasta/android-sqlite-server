package org.devtcg.sqliteserver.tests;

import android.content.Intent;
import android.database.Cursor;
import android.test.AndroidTestCase;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.SQLiteServerConnectionManager;
import org.devtcg.sqliteserver.exception.SQLiteServerException;
import org.devtcg.sqliteserver.tests.support.Constants;
import org.devtcg.sqliteserver.tests.support.CrashHelper;
import org.devtcg.sqliteserver.tests.support.CrashOtroProcess;
import org.devtcg.sqliteserver.tests.support.SeparateProcessContentProvider;

public class ProcessDeathTest extends AndroidTestCase {
    private SQLiteServerConnectionManager mConnMgr;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConnMgr = new SQLiteServerConnectionManager(getContext());
    }

    public void testContentProviderServerDeath() {
        SQLiteServerConnection conn = mConnMgr.openConnectionToContentProvider(
                SeparateProcessContentProvider.AUTHORITY);
        try {
            CrashHelper.synchronouslyCrash(getContext(), Constants.OTRO_PROCESS);
            Cursor cursor = conn.rawQuery("SELECT * FROM test", new String[] {});
            cursor.close();
            fail("Should not get here");
        } catch (SQLiteServerException e) {
            // Server died, we should throw...
        } finally {
            conn.close();
        }
    }
}
