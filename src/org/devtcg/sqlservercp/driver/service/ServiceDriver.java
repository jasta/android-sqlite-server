package org.devtcg.sqlservercp.driver.service;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqlservercp.SQLiteServerConnection;
import org.devtcg.sqlservercp.driver.SQLiteServerConnectionDriver;

public class ServiceDriver implements SQLiteServerConnectionDriver {
    public ServiceDriver(Context mContext, Intent serviceIntent) {
    }

    @Override
    public SQLiteServerConnection openConnection() {
        throw new UnsupportedOperationException("TODO");
    }
}
