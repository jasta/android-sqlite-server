package org.devtcg.sqlservercp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.BulkCursorDescriptor;
import android.database.Cursor;
import android.database.CursorToBulkCursorAdaptor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.*;
import org.devtcg.sqlservercp.driver.ContentObserverProxy;

public abstract class SQLiteServerContentProvider extends ContentProvider {
    public static final String KEY_SELECTION_ARGS = "selection_args";
    public static final String KEY_CURSOR_DESCRIPTOR = "cursor_descriptor";
    public static final String KEY_CONTENT_OBSERVER = "content_observer";

    protected SQLiteDatabase getReadableDatabase() {
        return getWritableDatabase();
    }

    protected abstract SQLiteDatabase getWritableDatabase();

    @Override
    public boolean onCreate() {
        // Warm the database...
        getWritableDatabase();
        return true;
    }

    protected String getProviderName() {
        return getClass().getSimpleName();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("TODO");
    }

    private Bundle doInit(String arg, Bundle extras) {
        // No-op, onCreate already called...
        return null;
    }

    private Bundle doExecRaw(String arg, Bundle extras) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(arg);
        return null;
    }

    private Bundle doRawQuery(String arg, Bundle extras) {
        SQLiteDatabase db = getReadableDatabase();

        // TODO: exception handling.  See DatabaseUtils.readExceptionFromParcel(...)
        Cursor cursor = db.rawQuery(arg, extras.getStringArray(KEY_SELECTION_ARGS));

        Bundle ret = new Bundle();
        if (cursor != null) {
            ContentObserverProxy contentObserver = extras.getParcelable(KEY_CONTENT_OBSERVER);
            CursorToBulkCursorAdaptor adaptor = new CursorToBulkCursorAdaptor(
                    cursor, contentObserver, getProviderName());
            BulkCursorDescriptor descriptor = adaptor.getBulkCursorDescriptor();
            ret.putParcelable(KEY_CURSOR_DESCRIPTOR, descriptor);
        } else {
            ret.putParcelable(KEY_CURSOR_DESCRIPTOR, null);
        }
        return ret;
    }

    @Override
    public Bundle call(String methodString, String arg, Bundle extras) {
        SQLiteServerMethod method = SQLiteServerMethod.valueOf(methodString);
        switch (method) {
            case INIT:
                return doInit(arg, extras);
            case EXEC_RAW:
                return doExecRaw(arg, extras);
            case RAW_QUERY:
                return doRawQuery(arg, extras);
            default:
                throw new IllegalArgumentException("No such method: " + methodString);
        }
    }
}
