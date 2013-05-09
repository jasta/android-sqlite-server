package org.devtcg.sqliteserver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

    /**
     * Opens a connection to a ContentProvider.
     *
     * @param contentProviderAuthority ContentProvider's authority as declared in the manifest.
     * @return A new server connection.
     * @throws UnsupportedOperationException If this method is invoked on a platform
     *     prior to Honeycomb.
     */
    public SQLiteServerConnection openConnectionToContentProvider(
            String contentProviderAuthority) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            throw new UnsupportedOperationException("ContentProvider server not supported on " +
                    "versions of Android prior to Honeycomb");
        }
        return doAcquire(new ContentProviderClient(
                mContext, contentProviderAuthority));
    }

    /**
     * Opens a connection to a Service.
     *
     * @param serviceIntent Intent matching the desired service.
     * @return A new server connection.
     */
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
