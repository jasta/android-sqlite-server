package org.devtcg.sqliteserver.impl.binder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Looper;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;
import org.devtcg.sqliteserver.impl.binder.protocol.*;

import static org.devtcg.sqliteserver.impl.binder.protocol.BeginTransactionCommand.BeginTransactionMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.DeleteCommand.DeleteMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.EndTransactionCommand.EndTransactionMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.ExecSQLCommand.ExecSQLMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.InsertCommand.InsertMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.RawQueryCommand.RawQueryMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.SetTransactionSuccessfulCommand.SetTransactionSuccessfulMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.UpdateCommand.UpdateMessage;

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
        new SetTransactionSuccessfulMessage(this).transact();
    }

    @Override
    public void endTransaction() {
        new EndTransactionMessage(this).transact();
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
        InsertMessage command = new InsertMessage(this, table, values);
        command.transact();
        return command.getInsertId();
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        UpdateMessage command = new UpdateMessage(this, table, values, whereClause, whereArgs);
        command.transact();
        return command.getAffectedRowsCount();
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        DeleteMessage command = new DeleteMessage(this, table, whereClause, whereArgs);
        command.transact();
        return command.getAffectedRowsCount();
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
