package org.devtcg.sqliteserver.tests.util;

import android.content.Context;
import org.devtcg.sqliteserver.SQLiteServerConnection;

class ContentProviderConnectionProvider extends AbstractConnectionProvider {
    private final String mAuthority;

    public ContentProviderConnectionProvider(Context context, String authority) {
        super(context);
        mAuthority = authority;
    }

    @Override
    public SQLiteServerConnection openConnection() {
        return getConnectionManager().openConnectionToContentProvider(mAuthority);
    }

    @Override
    public String toString() {
        return "ContentProviderConnectionProvider{authority=" + mAuthority + "}";
    }
}
