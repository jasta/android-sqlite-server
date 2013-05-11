package org.devtcg.sqliteserver;

import android.database.sqlite.SQLiteDatabase;

/**
 * Common interface for both {@link android.content.ContentProvider} backed and
 * {@link android.app.Service} backed instances.
 */
public interface SQLiteServer {
    /**
     * Open and return the database instance to be used by this server.  This method will
     * be called only once, lazily initialized on demand.
     */
    public SQLiteDatabase getWritableDatabase();

    /**
     * Access the server's name.  This is used for IPC identification purposes.
     *
     * @return Server's name which must be unique across the application.  Typically this is
     * {@code getClass().getName()}.
     */
    public String getServerName();
}
