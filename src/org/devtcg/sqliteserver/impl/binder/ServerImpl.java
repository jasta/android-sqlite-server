package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.protocol.AbstractCommandMessage;
import org.devtcg.sqliteserver.impl.binder.protocol.MethodName;

import static org.devtcg.sqliteserver.impl.binder.protocol.BeginTransactionCommand.BeginTransactionHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.ExecSQLCommand.ExecSQLHandler;
import static org.devtcg.sqliteserver.impl.binder.protocol.RawQueryCommand.RawQueryHandler;

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
     */
    public void forceClose() {
        mExecutor.close();
    }

    public Bundle onTransact(Bundle request) {
        int methodNameOrdinal = request.getInt(AbstractCommandMessage.KEY_METHOD_NAME);
        MethodName methodName = MethodName.values()[methodNameOrdinal];
        switch (methodName) {
            case BEGIN_TRANSACTION:
                return new BeginTransactionHandler(mExecutor).handle(request);
            case RAW_QUERY:
                return new RawQueryHandler(mExecutor, mServerName).handle(request);
            case EXEC_SQL:
                return new ExecSQLHandler(mExecutor).handle(request);
            default:
                throw new UnsupportedOperationException("TODO");
        }
    }
}
