package org.devtcg.sqliteserver.impl.binder;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

@TargetApi(11)
public class ContentProviderClient extends AbstractBinderClient {
    private final ContentResolver mResolver;
    private final Uri mAuthority;

    public ContentProviderClient(Context context, String authority) {
        mResolver = context.getContentResolver();
        mAuthority = Uri.parse("content://" + authority);
    }

    @Override
    protected Bundle doTransact(Bundle request) {
        // method name ("transact") is dummy, the real method name is encoded in the Bundle
        // for consistency with the service implementation.
        return mResolver.call(mAuthority, "transact", null, request);
    }
}
