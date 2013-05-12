package org.devtcg.sqliteserver.tests;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.tests.util.ConnectionProvider;
import org.devtcg.sqliteserver.tests.util.ConnectionProviders;

public class SQLiteServerExceptionTest extends AndroidTestCase {
    public void testTransactionFailure() {
        for (ConnectionProvider provider : ConnectionProviders.allConnections(getContext())) {
            SQLiteServerConnection conn = provider.openConnection();
            try {
                conn.endTransaction();
                fail("Expected IllegalStateException when communicating with provider " + provider);
            } catch (IllegalStateException e) {
                // Test pass.
            } finally {
                conn.close();
            }
        }
    }

    public void testBogusSQL() {
        for (ConnectionProvider provider : ConnectionProviders.allConnections(getContext())) {
            SQLiteServerConnection conn = provider.openConnection();
            try {
                conn.execSQL("does not compute");
                fail("Expected SQLiteException when communicating with provider " + provider);
            } catch (SQLiteException e) {
                // Test pass.
            } finally {
                conn.close();
            }
        }
    }
}
