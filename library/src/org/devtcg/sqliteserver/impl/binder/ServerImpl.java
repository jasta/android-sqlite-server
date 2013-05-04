package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.protocol.AbstractCommandMessage;
import org.devtcg.sqliteserver.impl.binder.protocol.MethodName;

import static org.devtcg.sqliteserver.impl.binder.protocol.BeginTransactionCommand.BeginTransactionHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.DeleteCommand.DeleteHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.EndTransactionCommand.EndTransactionHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.ExecSQLCommand.ExecSQLHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.InsertCommand.InsertHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.RawQueryCommand.RawQueryHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.SetTransactionSuccessfulCommand.SetTransactionSuccessfulHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.UpdateCommand.UpdateHandler;

/**
 * Unflattens server command messages expressed in a Bundle and delegates them to the
 * {@link SQLiteExecutor}.  Then, packages up the response back into a Bundle for
 * {@link AbstractBinderClient} to process on the other end.
 */
public class ServerImpl {
    private final SQLiteExecutor mExecutor;
    private final String mServerName;

    public ServerImpl(SQLiteExecutor executor, String serverName) {
        mExecutor = executor;
        mServerName = serverName;
    }

    /**
     * Used by the service implementation to trigger close.  Need to revisit
     * this policy at some point in the future.  It's hard to decide whether it's
     * best to have the ContentProvider and Service implementations try to match
     * each other semantically or if there is cause to offer a more flexible model
     * with the Service implementation.
     * <p>
     * The ContentProvider currently never closes the database, and never shuts down, per usual
     * with the ContentProvider paradigm.
     */
    public void closeDatabase() {
        mExecutor.close();
    }

    public Bundle onTransact(Bundle request) {
        int methodNameOrdinal = request.getInt(AbstractCommandMessage.KEY_METHOD_NAME);
        MethodName methodName = MethodName.values()[methodNameOrdinal];
        switch (methodName) {
            case BEGIN_TRANSACTION:
                return new BeginTransactionHandler(mExecutor).handle(request);
            case SET_TRANSACTION_SUCCESSFUL:
                return new SetTransactionSuccessfulHandler(mExecutor).handle(request);
            case END_TRANSACTION:
                return new EndTransactionHandler(mExecutor).handle(request);
            case RAW_QUERY:
                return new RawQueryHandler(mExecutor, mServerName).handle(request);
            case EXEC_SQL:
                return new ExecSQLHandler(mExecutor).handle(request);
            case INSERT:
                return new InsertHandler(mExecutor).handle(request);
            case UPDATE:
                return new UpdateHandler(mExecutor).handle(request);
            case DELETE:
                return new DeleteHandler(mExecutor).handle(request);
            default:
                throw new IllegalArgumentException("Unsupported methodName=" + methodName);
        }
    }
}
