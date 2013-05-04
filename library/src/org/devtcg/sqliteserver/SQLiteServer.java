package org.devtcg.sqliteserver;

import android.database.sqlite.SQLiteDatabase;

/**
 * Common interface for both {@link android.content.ContentProvider} backed and
 * {@link android.app.Service} backed instances.
 */
public interface SQLiteServer {
    public SQLiteDatabase getWritableDatabase();

    public String getServerName();
}
