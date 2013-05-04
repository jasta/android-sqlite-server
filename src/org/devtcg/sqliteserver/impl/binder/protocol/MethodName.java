package org.devtcg.sqliteserver.impl.binder.protocol;

public enum MethodName {
    BEGIN_TRANSACTION,
    SET_TRANSACTION_SUCCESSFUL,
    END_TRANSACTION,
    RAW_QUERY,
    EXEC_SQL,
    INSERT,
    UPDATE,
    DELETE,
}
