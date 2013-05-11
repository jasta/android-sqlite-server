package org.devtcg.sqliteserver.tests.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static final String DB_NAME_IN_MEMORY = null;

    public TestDatabaseOpenHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
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
}
