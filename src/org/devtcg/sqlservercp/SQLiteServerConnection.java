package org.devtcg.sqlservercp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import org.devtcg.sqlservercp.driver.contentprovider.SQLiteServerException;

public interface SQLiteServerConnection {
    public void beginTransaction();
    public void setTransactionSuccessful();
    public void endTransaction();
    public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLiteException,
            SQLiteServerException;
    public void execSQL(String sql);
    public long insert(String table, ContentValues values);
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs);
    public int delete(String table, String whereClause, String[] whereArgs);
    public void close();
}
