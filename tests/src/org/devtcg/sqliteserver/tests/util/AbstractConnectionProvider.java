package org.devtcg.sqliteserver.tests.util;

import android.content.Context;
import org.devtcg.sqliteserver.SQLiteServerConnectionManager;

public abstract class AbstractConnectionProvider implements ConnectionProvider {
    private final SQLiteServerConnectionManager mConnMgr;

    public AbstractConnectionProvider(Context context) {
        mConnMgr = new SQLiteServerConnectionManager(context);
    }

    public SQLiteServerConnectionManager getConnectionManager() {
        return mConnMgr;
    }
}
