package org.devtcg.sqliteserver;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.ServerImplProvider;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

public abstract class SQLiteContentProviderServer extends ContentProvider
        implements SQLiteServer {
    private ServerImplProvider mServerImplProvider;

    @Override
    public boolean onCreate() {
        // Warm the database...
        mServerImplProvider = new ServerImplProvider(this);
        mServerImplProvider.get();
        return true;
    }

    @Override
    public Bundle call(String methodString, String arg, Bundle extras) {
        return mServerImplProvider.get().onTransact(extras);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // This method is not used for cursors.  For consistency with the Service flow, we
        // take the hackier path of using the BulkCursorToCursorAdaptor/CursorToBulkCursorAdaptor
        // private APIs.
        throw new UnsupportedOperationException("Not part of the default implementation");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not part of the default implementation");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not part of the default implementation");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not part of the default implementation");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not part of the default implementation");
    }
}
