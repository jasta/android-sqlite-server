package org.devtcg.sqliteserver;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import org.devtcg.sqliteserver.exception.SQLiteServerException;

/**
 * Client interface for interacting with a server instance (either
 * {@link SQLiteContentProviderServer} or {@link SQLiteServiceServer}).
 * <p>
 * This API strives to provide all of the same features as
 * {@link SQLiteDatabase}, making it a perfect drop-in replacements.
 */
public interface SQLiteServerConnection {
    /**
     * @see SQLiteDatabase#beginTransaction()
     */
    public void beginTransaction();

    /**
     * @see SQLiteDatabase#setTransactionSuccessful()
     */
    public void setTransactionSuccessful();

    /**
     * @see SQLiteDatabase#endTransaction()
     */
    public void endTransaction();

    /**
     * @see SQLiteDatabase#rawQuery(String, String[])
     */
    public Cursor rawQuery(String sql, String[] selectionArgs);

    /**
     * @see SQLiteDatabase#execSQL(String)
     */
    public void execSQL(String sql);

    /**
     * @see SQLiteDatabase#insert(String, String, android.content.ContentValues)
     */
    public long insert(String table, ContentValues values);

    /**
     * @see SQLiteDatabase#update(String, android.content.ContentValues, String, String[])
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs);

    /**
     * @see SQLiteDatabase#delete(String, String, String[])
     */
    public int delete(String table, String whereClause, String[] whereArgs);

    /**
     * Close the database connection.  Note that this does not necessarily cause the server
     * to close its database handle.
     */
    public void close();
}
