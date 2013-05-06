package org.devtcg.sqliteserver.impl.binder.protocol;

public enum MethodName {
    /**
     * Special command executed for each new Binder-based SQLiteServerConnection.  This is used
     * to establish the Binder relationship between both peers so that we can take advantage of
     * asynchronous death notifications.
     */
    ACQUIRE,

    BEGIN_TRANSACTION,
    SET_TRANSACTION_SUCCESSFUL,
    END_TRANSACTION,
    RAW_QUERY,
    EXEC_SQL,
    INSERT,
    UPDATE,
    DELETE,

    /**
     * Release the Binder relationship.  See {@link #ACQUIRE}.
     */
    RELEASE,
}
