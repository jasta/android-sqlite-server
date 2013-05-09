package org.devtcg.sqliteserver.sample;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.SQLiteServerConnectionManager;

import java.util.ArrayList;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        new Thread() {
            public void run() {
                smokeTest();
            }
        }.start();
    }

    private abstract class AbstractConnectionProvider {
        protected SQLiteServerConnectionManager createConnectionManager() {
            return new SQLiteServerConnectionManager(getApplicationContext());
        }

        public abstract SQLiteServerConnection openConnection();
    }

    private class ServiceConnectionProvider extends AbstractConnectionProvider {
        @Override
        public SQLiteServerConnection openConnection() {
            Intent serviceIntent = new Intent(MyActivity.this, TestService.class);
            System.out.println("Opening Service connection to: " + serviceIntent.getComponent());
            return createConnectionManager().openConnectionToService(serviceIntent);
        }
    }

    private class ContentProviderConnectionProvider extends AbstractConnectionProvider {
        @Override
        public SQLiteServerConnection openConnection() {
            String authority = TestContentProvider.AUTHORITY;
            System.out.println("Opening ContentProvider connection to: " + authority);
            return createConnectionManager().openConnectionToContentProvider(authority);
        }
    }

    private void smokeTest() {
        ArrayList<AbstractConnectionProvider> providers =
                new ArrayList<AbstractConnectionProvider>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            providers.add(new ContentProviderConnectionProvider());
        }
        providers.add(new ServiceConnectionProvider());
        for (AbstractConnectionProvider provider : providers) {
            SQLiteServerConnection conn = provider.openConnection();
            try {
                doSmokeTest(conn);
            } finally {
                conn.close();
            }
        }
    }

    private void doSmokeTest(SQLiteServerConnection conn) {
        conn.beginTransaction();
        try {
            System.out.println("Deleting all records...");
            conn.delete("test", null, null);

            System.out.println("Inserting records...");
            ContentValues values = new ContentValues();
            values.put("test1", "foo");
            values.put("test2", "bar");
            conn.insert("test", values);
            values.put("test2", "baz");
            conn.insert("test", values);
            values.put("test1", "bla");
            conn.insert("test", values);
            conn.setTransactionSuccessful();
        } finally {
            conn.endTransaction();
        }

        System.out.println("Querying...");
        Cursor foo = conn.rawQuery("SELECT * FROM test", new String[] {});
        try {
            dumpCursor(foo);
        } finally {
            foo.close();
        }
    }

    private static void dumpCursor(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        System.out.println("count=" + cursor.getCount() + "; columnCount=" + columnCount);
        int rowNum = 0;
        while (cursor.moveToNext()) {
            System.out.println("Row #" + (++rowNum) + ":");
            for (int i = 0; i < columnCount; i++) {
                System.out.println("\t" + cursor.getColumnName(i) + ": " +
                        cursor.getString(i));
            }
        }
    }

}
