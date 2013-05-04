package org.devtcg.sqlservercp;

import android.database.SQLException;

public class SQLiteServerException extends RuntimeException {
    public SQLiteServerException(String msg) {
        super(msg);
    }

    public SQLiteServerException(Throwable t) {
        super(t);
    }
}
