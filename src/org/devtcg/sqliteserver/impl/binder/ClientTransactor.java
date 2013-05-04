package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;

public interface ClientTransactor {
    /**
     * Execute a client request (submit it to our server peer).
     */
    public Bundle transact(Bundle request);
}
