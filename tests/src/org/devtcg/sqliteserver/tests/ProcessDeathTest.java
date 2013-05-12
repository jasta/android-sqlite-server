package org.devtcg.sqliteserver.tests;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;
import org.devtcg.sqliteserver.tests.support.Constants;
import org.devtcg.sqliteserver.tests.support.delegate.TestDelegateService;
import org.devtcg.sqliteserver.tests.support.delegate.command.BeginTransactionCommand;
import org.devtcg.sqliteserver.tests.support.delegate.command.Command;
import org.devtcg.sqliteserver.tests.support.delegate.command.InsertCommand;
import org.devtcg.sqliteserver.tests.support.delegate.command.OpenConnectionCommand;
import org.devtcg.sqliteserver.tests.util.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.devtcg.sqliteserver.tests.util.DbHelper.assertEmpty;

public class ProcessDeathTest extends AndroidTestCase {
    public void testServerDeath() {
        for (ConnectionProvider provider :
                ConnectionProviders.separateProcessConnections(getContext())) {
            SQLiteServerConnection conn = provider.openConnection();
            try {
                CrashHelper.synchronouslyCrash(getContext(), Constants.OTRO_PROCESS);
                conn.rawQuery("SELECT * FROM test", new String[] {});
                fail("Should not get here (from connection=" + provider + ")");
            } catch (SQLiteServerException e) {
                // Server died, which is what we expected...
            } finally {
                conn.close();
            }
        }
    }

    // Note: the "client" is actually in the test delegate process; the test process
    // is the server.
    public void testClientDeath() throws IOException {
        for (ConnectionProvider provider :
                ConnectionProviders.sameProcessConnections(getContext())) {
            SQLiteServerConnection localConn = provider.openConnection();
            localConn.delete("test", null, null);
            assertEmpty(localConn.rawQuery("SELECT * FROM test", new String[] {}));

            Command openCommand;
            if (provider instanceof ContentProviderConnectionProvider) {
                openCommand = new OpenConnectionCommand(
                        ((ContentProviderConnectionProvider)provider).getAuthority());
            } else if (provider instanceof ServiceConnectionProvider) {
                openCommand = new OpenConnectionCommand(
                        ((ServiceConnectionProvider)provider).getServiceIntent());
            } else {
                throw new IllegalStateException("Did you forget to add something here?");
            }

            TestDelegateService.sendCommand(getContext(), openCommand);
            TestDelegateService.sendCommand(getContext(), new BeginTransactionCommand());

            // Inserting inside a transaction should be isolated from each thread/process.
            ContentValues values = new ContentValues();
            values.put("test1", "foo");
            values.put("test2", "bar");
            TestDelegateService.sendCommand(getContext(), new InsertCommand("test", values));

            // Make sure the service executes all of our commands up to this point.
            TestDelegateService.synchronizeState(getContext(), 4000, TimeUnit.MILLISECONDS);

            // ...and then take it out.
            CrashHelper.synchronouslyCrash(getContext(), Constants.TEST_DELEGATE_PROCESS);

            // Make sure the transaction was therefore rolled back.
            assertEmpty(localConn.rawQuery("SELECT * FROM test", new String[] {}));
        }
    }
}
