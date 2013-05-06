package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;

public interface ClientTransactor {
    /**
     * Get the client's binder handle.  This uniquely identifies this client connection to the
     * server and must not change after the connection is made.
     */
    public BinderHandle getClientHandle();

    /**
     * Execute a client request (submit it to our server peer).
     */
    public Bundle transact(Bundle request);
}
