package org.devtcg.sqliteserver.impl.binder;

/**
 * Thrown if any part of the IPC contract is not met when communicating with either the client or
 * server party in the SQLiteServerConnection exchange.
 */
public class SQLiteServerProtocolException extends Exception {
    public SQLiteServerProtocolException() {
        super();
    }

    public SQLiteServerProtocolException(String message) {
        super(message);
    }

    public SQLiteServerProtocolException(String message, Throwable t) {
        super(message, t);
    }

    public SQLiteServerProtocolException(Throwable t) {
        super(t);
    }
}
