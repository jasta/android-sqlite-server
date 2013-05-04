package org.devtcg.sqliteserver;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqliteserver.impl.binder.ContentProviderClient;
import org.devtcg.sqliteserver.impl.binder.ServiceClient;

public class SQLiteServerConnectionManager {
    private final Context mContext;

    public SQLiteServerConnectionManager(Context context) {
        mContext = context;
    }

    public SQLiteServerConnection openConnectionToContentProvider(
            String contentProviderAuthority) {
        return new ContentProviderClient(mContext, contentProviderAuthority);
    }

    public SQLiteServerConnection openConnectionToService(
            Intent serviceIntent) {
        return new ServiceClient(mContext, serviceIntent);
    }
}
