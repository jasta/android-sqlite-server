package org.devtcg.sqlservercp.driver;

import org.devtcg.sqlservercp.SQLiteServerConnection;

public interface SQLiteServerConnectionDriver {
    public SQLiteServerConnection openConnection();
}