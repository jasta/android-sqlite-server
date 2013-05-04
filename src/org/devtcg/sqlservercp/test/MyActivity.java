package org.devtcg.sqlservercp.test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import org.devtcg.sqlservercp.R;
import org.devtcg.sqlservercp.SQLiteServerConnection;
import org.devtcg.sqlservercp.SQLiteServerConnectionManager;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
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

    private void smokeTest() {
        SQLiteServerConnectionManager mgr = new SQLiteServerConnectionManager(
                getApplicationContext());
        SQLiteServerConnection conn = mgr.openConnectionToContentProvider(
                TestContentProvider.AUTHORITY);

        System.out.println("Deleting all records...");
        conn.execSQL("DELETE FROM test");

        System.out.println("Inserting records...");
        conn.execSQL("INSERT INTO test (test1, test2) VALUES ('foo', 'bar')");
        conn.execSQL("INSERT INTO test (test1, test2) VALUES ('foo', 'baz')");
        conn.execSQL("INSERT INTO test (test1, test2) VALUES ('man', 'baz')");

        System.out.println("Querying...");
        Cursor foo = conn.rawQuery("SELECT * FROM test", new String[] {});
        try {
            dumpCursor(foo);
        } finally {
            foo.close();
        }
        conn.close();
    }

    private static void dumpCursor(Cursor cursor) {
        int columnCount = cursor.getColumnCount();
        System.out.println("count=" + cursor.getCount() + "; columnCount=" + columnCount);
        int rowNum = 0;
        while (cursor.moveToNext()) {
            System.out.println("Row #" + (++rowNum) + ":");
            for (int i = 0; i < columnCount; i++) {
                System.out.print("\t" + cursor.getColumnName(i) + ": ");
                int type = cursor.getType(i);
                Object value;
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        value = cursor.getString(i);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        value = cursor.getInt(i);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                    default:
                        value = null;
                        break;
                }
                System.out.println(value != null ? value.toString() : "null");
            }
        }
    }

}
