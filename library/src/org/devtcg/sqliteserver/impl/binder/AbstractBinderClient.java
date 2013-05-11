package org.devtcg.sqliteserver.impl.binder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.exception.SQLiteServerException;

import static android.os.IBinder.DeathRecipient;
import static org.devtcg.sqliteserver.impl.binder.protocol.AcquireCommand.AcquireMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.BeginTransactionCommand.BeginTransactionMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.DeleteCommand.DeleteMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.EndTransactionCommand.EndTransactionMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.ExecSQLCommand.ExecSQLMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.InsertCommand.InsertMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.RawQueryCommand.RawQueryMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.ReleaseCommand.ReleaseMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.SetTransactionSuccessfulCommand.SetTransactionSuccessfulMessage;
import static org.devtcg.sqliteserver.impl.binder.protocol.UpdateCommand.UpdateMessage;

/**
 * Client counterpart to {@link ServerImpl} that takes client requests, packages them up in a
 * generic {@link Bundle} and sends them along to the server implementation.
 */
public abstract class AbstractBinderClient implements SQLiteServerConnection, ClientTransactor {
    private final String mTag = getClass().getSimpleName();

    private BinderHandle mClientHandle;
    private BinderHandle mServerHandle;

    private DeathRecipient mServerDiedHandler;

    private boolean mClosed;
    private volatile boolean mDeadServer;

    @Override
    public BinderHandle getClientHandle() {
        if (mClientHandle == null) {
            mClientHandle = new BinderHandle();
        }
        return mClientHandle;
    }

    public void acquire() throws SQLiteServerProtocolException {
        AcquireMessage message = new AcquireMessage(this);
        message.transact();
        mServerHandle = message.getServerHandle();
        try {
            mServerDiedHandler = new ServerDiedHandler(mServerHandle);
            mServerHandle.asBinder().linkToDeath(mServerDiedHandler, 0);
        } catch (RemoteException e) {
            throw new SQLiteServerProtocolException("Server is not supposed to die!", e);
        }
    }

    public void release() throws SQLiteServerProtocolException {
        if (mServerHandle != null) {
            mServerHandle.asBinder().unlinkToDeath(mServerDiedHandler, 0);
            new ReleaseMessage(this).transact();
        }
    }

    @Override
    public void close() {
        try {
            if (!mDeadServer) {
                release();
            }
        } catch (SQLiteServerProtocolException e) {
            // Not much we can do, but this may also not be harmless so we'll log just in case
            Log.i(mTag, "Failed to release connection with the server, ignoring...");
        }
        mClosed = true;
    }

    private void checkReady() {
        if (mClosed) {
            throw new IllegalStateException("Illegal to access this connection after it has " +
                    "been closed");
        }
        if (mDeadServer) {
            // TODO: try to bring it back up and wait for it to become alive again.  This
            // should be possible by just re-acquiring and re-executing the last failed
            // command?
            throw new SQLiteServerException("Server has died unexpectedly; you must open a " +
                    "new connection");
        }
    }

    @Override
    public void beginTransaction() {
        checkReady();
        new BeginTransactionMessage(this).transact();
    }

    @Override
    public void setTransactionSuccessful() {
        checkReady();
        new SetTransactionSuccessfulMessage(this).transact();
    }

    @Override
    public void endTransaction() {
        checkReady();
        new EndTransactionMessage(this).transact();
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) throws SQLiteException, SQLiteServerException {
        checkReady();
        RawQueryMessage command = new RawQueryMessage(this, sql, selectionArgs);
        command.transact();
        return command.getCursor();
    }

    @Override
    public void execSQL(String sql) {
        checkReady();
        new ExecSQLMessage(this, sql).transact();
    }

    @Override
    public long insert(String table, ContentValues values) {
        checkReady();
        InsertMessage command = new InsertMessage(this, table, values);
        command.transact();
        return command.getInsertId();
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        checkReady();
        UpdateMessage command = new UpdateMessage(this, table, values, whereClause, whereArgs);
        command.transact();
        return command.getAffectedRowsCount();
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        checkReady();
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

        request.setClassLoader(getClass().getClassLoader());
        Bundle response = doTransact(request);
        if (response != null) {
            response.setClassLoader(getClass().getClassLoader());
        }
        return response;
    }

    /**
     * Blocking call to the server peer to deliver our request and receive the response.
     *
     * @param request Request encoded as a {@link Bundle}.
     * @return Response encoded as a {@link Bundle}.
     */
    protected abstract Bundle doTransact(Bundle request);

    private class ServerDiedHandler implements DeathRecipient {
        private final BinderHandle mServerHandle;

        public ServerDiedHandler(BinderHandle serverHandle) {
            mServerHandle = serverHandle;
        }

        @Override
        public void binderDied() {
            Log.w(mTag, "Unexpected server death (serverHandle=" + mServerHandle + ")");
            mDeadServer = true;
        }
    }
}
