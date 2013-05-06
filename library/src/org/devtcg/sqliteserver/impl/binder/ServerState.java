package org.devtcg.sqliteserver.impl.binder;

public class ServerState {
    public BinderHandle clientHandle;

    /**
     * Counter for the number of active transactions the client has initiated.
     */
    public int numTransactions;
}
