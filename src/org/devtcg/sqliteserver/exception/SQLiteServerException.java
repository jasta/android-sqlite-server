package org.devtcg.sqliteserver.exception;

public class SQLiteServerException extends RuntimeException {
    public SQLiteServerException(String msg) {
        super(msg);
    }

    public SQLiteServerException(Throwable t) {
        super(t);
    }
}
