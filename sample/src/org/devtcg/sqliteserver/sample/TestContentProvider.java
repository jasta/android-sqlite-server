package org.devtcg.sqliteserver.sample;

import android.database.sqlite.SQLiteDatabase;
import org.devtcg.sqliteserver.SQLiteContentProviderServer;

public class TestContentProvider extends SQLiteContentProviderServer {
    private static final String DB_NAME = "test_cp.db";
    private static final int DB_VERSION = 2;

    static final String AUTHORITY = "org.devtcg.sqliteserver.testcp";

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return new MyOpenHelper(getContext(), DB_NAME, DB_VERSION).getWritableDatabase();
    }

    @Override
    public String getServerName() {
        return getClass().getName();
    }
}
