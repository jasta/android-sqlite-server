package org.devtcg.sqliteserver.tests.util;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqliteserver.SQLiteServerConnection;

class ServiceConnectionProvider extends AbstractConnectionProvider {
    private final Intent mServiceIntent;

    public ServiceConnectionProvider(Context context, Intent serviceIntent) {
        super(context);
        mServiceIntent = serviceIntent;
    }

    @Override
    public SQLiteServerConnection openConnection() {
        return getConnectionManager().openConnectionToService(mServiceIntent);
    }

    @Override
    public String toString() {
        return "ServiceConnectionProvider{intent=" + mServiceIntent + "}";
    }
}
