package org.devtcg.sqlservercp.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.devtcg.sqlservercp.SQLiteServerContentProvider;

public class TestContentProvider extends SQLiteServerContentProvider {
    private static final String DB_NAME = "test.db";
    private static final int DB_VERSION = 2;

    static final String AUTHORITY = "org.devtcg.sqlservercp.testcp";

    private MyOpenHelper mOpenHelper;

    // TODO: Would be nice to introduce a SQLiteConnectionOpenHelper that provides the same
    // functionality so that the ContentProvider is just a shell.
    private class MyOpenHelper extends SQLiteOpenHelper {
        public MyOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        private void dropTables(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS test");
        }

        private void createTables(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE test (" +
                    "_id INTEGER PRIMARY KEY, " +
                    "test1 TEXT, " +
                    "test2 TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropTables(db);
            createTables(db);
        }
    }

    @Override
    protected SQLiteDatabase getWritableDatabase() {
        if (mOpenHelper == null) {
            mOpenHelper = new MyOpenHelper(getContext());
        }
        return mOpenHelper.getWritableDatabase();
    }
}
