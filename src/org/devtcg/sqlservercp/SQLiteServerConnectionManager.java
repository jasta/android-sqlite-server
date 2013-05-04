package org.devtcg.sqlservercp;

import android.content.Context;
import android.content.Intent;
import org.devtcg.sqlservercp.driver.contentprovider.ContentProviderDriver;
import org.devtcg.sqlservercp.driver.service.ServiceDriver;

public class SQLiteServerConnectionManager {
    private final Context mContext;

    public SQLiteServerConnectionManager(Context context) {
        mContext = context;
    }

    public SQLiteServerConnection openConnectionToContentProvider(
            String contentProviderAuthority) {
        return new ContentProviderDriver(mContext, contentProviderAuthority).openConnection();
    }

    public SQLiteServerConnection openConnectionToService(
            Intent serviceIntent) {
        return new ServiceDriver(mContext, serviceIntent).openConnection();
    }
}
