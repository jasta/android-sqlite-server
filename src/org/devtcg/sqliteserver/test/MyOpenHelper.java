package org.devtcg.sqliteserver.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// TODO: Would be nice to introduce a SQLiteConnectionOpenHelper that provides the same
// functionality so that the ContentProvider is just a shell.
class MyOpenHelper extends SQLiteOpenHelper {
    public MyOpenHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
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
