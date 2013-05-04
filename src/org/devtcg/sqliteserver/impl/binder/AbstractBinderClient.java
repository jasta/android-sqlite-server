package org.devtcg.sqliteserver.impl.binder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Looper;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;

import static org.devtcg.sqliteserver.impl.binder.protocol.BeginTransactionCommand.BeginTransactionMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.ExecSQLCommand.ExecSQLMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.RawQueryCommand.RawQueryMessage;

/**
 * Client counterpart to {@link ServerImpl} that takes client requests, packages them up in a
 * generic {@link Bundle} and sends them along to the server implementation.
 */
public abstract class AbstractBinderClient implements SQLiteServerConnection, ClientTransactor {
    @Override
    public void beginTransaction() {
        new BeginTransactionMessage(this).transact();
    }

    @Override
    public void setTransactionSuccessful() {
        throw new UnsupportedOperationException("TODO");
//        new SetTransactionSuccessfulMessage(this).transact();
    }

    @Override
    public void endTransaction() {
        throw new UnsupportedOperationException("TODO");
//        new EndTransactionMessage(this).transact();
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLiteException, SQLiteServerException {
        RawQueryMessage command = new RawQueryMessage(this, sql, selectionArgs);
        command.transact();
        return command.getCursor();
    }

    @Override
    public void execSQL(String sql) {
        new ExecSQLMessage(this, sql).transact();
    }

    @Override
    public long insert(String table, ContentValues values) {
        throw new UnsupportedOperationException("TODO");
//        InsertMessage command = new InsertMessage(this, table, values);
//        command.transact();
//        return command.getInsertId();
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        throw new UnsupportedOperationException("TODO");
//        UpdateMessage command = new UpdateMessage(this, table, values, whereClause, whereArgs);
//        command.transact();
//        return command.getAffectedRowsCount();
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        throw new UnsupportedOperationException("TODO");
//        DeleteMessage command = new DeleteMessage(this, table, whereClause, whereArgs);
//        command.transact();
//        return command.getAffectedRowsCount();
    }

    @Override
    public Bundle transact(Bundle request) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IllegalStateException("SQLiteServiceConnection operations are not allowed " +
                    "on the main thread by design");
        }

        return doTransact(request);
    }

    /**
     * Blocking call to the server peer to deliver our request and receive the response.
     *
     * @param request Request encoded as a {@link Bundle}.
     * @return Response encoded as a {@link Bundle}.
     */
    protected abstract Bundle doTransact(Bundle request);
}
