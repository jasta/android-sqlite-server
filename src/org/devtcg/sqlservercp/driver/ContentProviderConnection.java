package org.devtcg.sqlservercp.driver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.BulkCursorDescriptor;
import android.database.BulkCursorToCursorAdaptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import org.devtcg.sqlservercp.SQLiteServerConnection;
import org.devtcg.sqlservercp.SQLiteServerContentProvider;
import org.devtcg.sqlservercp.SQLiteServerException;
import org.devtcg.sqlservercp.SQLiteServerMethod;
import org.devtcg.sqlservercp.driver.contentprovider.SQLiteServerException;

public class ContentProviderConnection implements SQLiteServerConnection {
    private final ContentResolver mResolver;
    private final Uri mAuthority;

    public ContentProviderConnection(ContentResolver resolver, Uri authority) {
        mResolver = resolver;
        mAuthority = authority;

        init();
    }

    private void init() {
        mResolver.call(mAuthority, SQLiteServerMethod.INIT.toString(), null, null);
    }

    @Override
    public void beginTransaction() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void setTransactionSuccessful() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void endTransaction() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        BulkCursorToCursorAdaptor adaptor = new BulkCursorToCursorAdaptor();
        try {
            Bundle args = new Bundle();
            args.putStringArray(SQLiteServerContentProvider.KEY_SELECTION_ARGS, selectionArgs);
            args.putParcelable(SQLiteServerContentProvider.KEY_CONTENT_OBSERVER,
                    new ContentObserverProxy(adaptor.getObserver()));
            Bundle ret = mResolver.call(mAuthority, SQLiteServerMethod.RAW_QUERY.toString(),
                    sql, args);
            if (ret == null) {
                throw new RemoteException();
            }
            BulkCursorDescriptor descriptor = ret.getParcelable(
                    SQLiteServerContentProvider.KEY_CURSOR_DESCRIPTOR);
            if (descriptor != null) {
                adaptor.initialize(descriptor);
                return adaptor;
            } else {
                adaptor.close();
                return null;
            }
        } catch (RemoteException e) {
            adaptor.close();
            throw new SQLiteServerException(e);
        } catch (RuntimeException e) {
            adaptor.close();
            throw e;
        }
    }

    @Override
    public void execSQL(String sql) {
        mResolver.call(mAuthority, SQLiteServerMethod.EXEC_RAW.toString(), sql, null);
    }

    @Override
    public long insert(String table, ContentValues values) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void close() {
        // Eh, not sure what model I want just yet.
    }
}
