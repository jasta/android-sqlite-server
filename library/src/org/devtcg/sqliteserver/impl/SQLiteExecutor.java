package org.devtcg.sqliteserver.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;

/**
 * Local implementation that delegates protocol commands to the actual
 * {@link SQLiteDatabase}.
 */
public class SQLiteExecutor implements SQLiteServerConnection {
    private final SQLiteDatabase mDatabase;

    public SQLiteExecutor(SQLiteDatabase database) {
        mDatabase = database;
    }

    @Override
    public void beginTransaction() {
        mDatabase.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        mDatabase.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        mDatabase.endTransaction();
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLiteException, SQLiteServerException {
        return mDatabase.rawQuery(sql, selectionArgs);
    }

    @Override
    public void execSQL(String sql) {
        mDatabase.execSQL(sql);
    }

    @Override
    public long insert(String table, ContentValues values) {
        return mDatabase.insert(table, null, values);
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return mDatabase.update(table, values, whereClause, whereArgs);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        return mDatabase.delete(table, whereClause, whereArgs);
    }

    @Override
    public void close() {
        mDatabase.close();
    }
}
