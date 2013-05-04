package org.devtcg.sqlservercp;

public enum SQLiteServerMethod {
    /** Initialize the server. */
    INIT,

    /** Query. */
    RAW_QUERY,

    /** Execute raw SQL. */
    EXEC_RAW,

    /** Shutdown the server. */
    SHUTDOWN,
}
