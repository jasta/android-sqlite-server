package org.devtcg.sqliteserver.tests;

import android.test.AndroidTestCase;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;
import org.devtcg.sqliteserver.tests.support.Constants;
import org.devtcg.sqliteserver.tests.util.ConnectionProvider;
import org.devtcg.sqliteserver.tests.util.ConnectionProviders;
import org.devtcg.sqliteserver.tests.util.CrashHelper;

public class ProcessDeathTest extends AndroidTestCase {
    public void testContentProviderServerDeath() {
        for (ConnectionProvider provider :
                ConnectionProviders.separateProcessConnections(getContext())) {
            SQLiteServerConnection conn = provider.openConnection();
            try {
                CrashHelper.synchronouslyCrash(getContext(), Constants.OTRO_PROCESS);
                conn.rawQuery("SELECT * FROM test", new String[] {});
                fail("Should not get here (from connection=" + provider + ")");
            } catch (SQLiteServerException e) {
                // Server died, we should throw...
            } finally {
                conn.close();
            }
        }
    }
}
