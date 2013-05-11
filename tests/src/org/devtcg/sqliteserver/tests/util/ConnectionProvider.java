package org.devtcg.sqliteserver.tests.util;

import org.devtcg.sqliteserver.SQLiteServerConnection;

public interface ConnectionProvider {
    public SQLiteServerConnection openConnection();
}
