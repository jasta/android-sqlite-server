package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.*;

/**
 * Server representation of the RPC call.  Implements the call and formats a response
 * for the client.
 */
public abstract class AbstractCommandHandler {
    private final ServerImpl mServerImpl;
    private BinderHandle mClientHandle;

    public AbstractCommandHandler(ServerImpl serverImpl) {
        mServerImpl = serverImpl;
    }

    protected final ServerImpl getServerImpl() {
        return mServerImpl;
    }

    protected final ServerState getServerState() {
        return mServerImpl.getServerState(mClientHandle);
    }

    final BinderHandle getClientHandle() {
        return mClientHandle;
    }

    protected final SQLiteExecutor getExecutor() {
        return mServerImpl.getExecutor();
    }

    public Bundle handle(Bundle request) throws SQLiteServerProtocolException {
        mClientHandle = BundleUtils.getParcelableOrThrow(request,
                AbstractCommandMessage.KEY_CLIENT_BINDER);

        // TODO: handle exceptions!
        Bundle result = onHandle(request);
        if (result == null) {
            return new Bundle();
        } else {
            return result;
        }
    }

    protected abstract Bundle onHandle(Bundle request) throws SQLiteServerProtocolException;
}
