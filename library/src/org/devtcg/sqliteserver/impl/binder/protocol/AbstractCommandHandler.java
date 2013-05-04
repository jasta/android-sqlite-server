package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;

/**
 * Server representation of the RPC call.  Implements the call and formats a response
 * for the client.
 */
public abstract class AbstractCommandHandler {
    private final SQLiteExecutor mExecutor;

    public AbstractCommandHandler(SQLiteExecutor executor) {
        mExecutor = executor;
    }

    protected final SQLiteExecutor getExecutor() {
        return mExecutor;
    }

    public Bundle handle(Bundle request) {
        // TODO: handle exceptions!
        Bundle result = onHandle(request);
        if (result == null) {
            return new Bundle();
        } else {
            return result;
        }
    }

    protected abstract Bundle onHandle(Bundle request);
}
