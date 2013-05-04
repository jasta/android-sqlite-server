package org.devtcg.sqlservercp.driver.contentprovider;

import android.content.Context;
import android.net.Uri;
import org.devtcg.sqlservercp.SQLiteServerConnection;
import org.devtcg.sqlservercp.driver.ContentProviderConnection;
import org.devtcg.sqlservercp.driver.SQLiteServerConnectionDriver;

public class ContentProviderDriver implements SQLiteServerConnectionDriver {
    private final Context mContext;
    private final Uri mAuthority;

    public ContentProviderDriver(Context context, String authority) {
        mContext = context;
        mAuthority = Uri.parse("content://" + authority);
    }

    public SQLiteServerConnection openConnection() {
        return new ContentProviderConnection(mContext.getContentResolver(), mAuthority);
    }
}
