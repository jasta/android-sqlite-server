package org.devtcg.sqliteserver;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqliteserver.exception.SQLiteServerException;
import org.devtcg.sqliteserver.impl.binder.AbstractBinderClient;
import org.devtcg.sqliteserver.impl.binder.ContentProviderClient;
import org.devtcg.sqliteserver.impl.binder.SQLiteServerProtocolException;
import org.devtcg.sqliteserver.impl.binder.ServiceClient;

public class SQLiteServerConnectionManager {
    private final Context mContext;

    public SQLiteServerConnectionManager(Context context) {
        mContext = context;
    }

    public SQLiteServerConnection openConnectionToContentProvider(
            String contentProviderAuthority) {
        return doAcquire(new ContentProviderClient(
                mContext, contentProviderAuthority));
    }

    public SQLiteServerConnection openConnectionToService(
            Intent serviceIntent) {
        return doAcquire(new ServiceClient(mContext, serviceIntent));
    }

    private AbstractBinderClient doAcquire(AbstractBinderClient client) {
        try {
            client.acquire();
            return client;
        } catch (SQLiteServerProtocolException e) {
            throw new SQLiteServerException(e);
        }
    }
}
