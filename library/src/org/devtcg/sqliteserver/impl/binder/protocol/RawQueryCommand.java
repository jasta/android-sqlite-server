package org.devtcg.sqliteserver.impl.binder.protocol;

import android.database.BulkCursorDescriptor;
import android.database.BulkCursorToCursorAdaptor;
import android.database.Cursor;
import android.database.CursorToBulkCursorAdaptor;
import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.ContentObserverProxy;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

public class RawQueryCommand {
    private static final String KEY_QUERY = "query";
    private static final String KEY_SELECTION_ARGS = "selection_args";
    private static final String KEY_CONTENT_OBSERVER = "content_observer";
    private static final String KEY_CURSOR_DESCRIPTOR = "cursor_descriptor";

    public static class RawQueryMessage extends AbstractCommandMessage {
        private final String mQuery;
        private final String[] mSelectionArgs;

        private final BulkCursorToCursorAdaptor mCursorAdaptor;

        private Cursor mCursor;

        public RawQueryMessage(
                ClientTransactor transactor,
                String query,
                String[] selectionArgs) {
            super(transactor, MethodName.RAW_QUERY);
            mQuery = query;
            mSelectionArgs = selectionArgs;

            // XXX: Serious gotcha here.  The ContentProviderNative code that implements this
            // class expects to be able to catch RuntimeException at any point during
            // processing and invoke close() on this instance if it does.  Our design
            // doesn't allow this, so we have no way of cleaning up whatever resources
            // are being used underneath on error!
            mCursorAdaptor = new BulkCursorToCursorAdaptor();
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString(KEY_QUERY, mQuery);
            request.putStringArray(KEY_SELECTION_ARGS, mSelectionArgs);
            request.putParcelable(KEY_CONTENT_OBSERVER,
                    new ContentObserverProxy(mCursorAdaptor.getObserver()));
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            try {
                BulkCursorDescriptor descriptor = response.getParcelable(KEY_CURSOR_DESCRIPTOR);
                if (descriptor != null) {
                    mCursorAdaptor.initialize(descriptor);
                    mCursor = mCursorAdaptor;
                    return;
                }
            } catch (RuntimeException e) {
                // Fall through...
            }
            mCursorAdaptor.close();
            mCursor = null;
        }

        public Cursor getCursor() {
            return mCursor;
        }
    }

    public static class RawQueryHandler extends AbstractCommandHandler {
        private final String mServerName;

        public RawQueryHandler(ServerImpl serverImpl, String serverName) {
            super(serverImpl);
            mServerName = serverName;
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            String query = request.getString(KEY_QUERY);
            String[] selectionArgs = request.getStringArray(KEY_SELECTION_ARGS);
            ContentObserverProxy contentObserver = request.getParcelable(KEY_CONTENT_OBSERVER);
            Cursor cursor = getExecutor().rawQuery(query, selectionArgs);
            Bundle ret = new Bundle();
            if (cursor != null) {
                CursorToBulkCursorAdaptor adaptor = new CursorToBulkCursorAdaptor(
                        cursor, contentObserver, mServerName);
                BulkCursorDescriptor descriptor = adaptor.getBulkCursorDescriptor();
                ret.putParcelable(KEY_CURSOR_DESCRIPTOR, descriptor);
            } else {
                ret.putParcelable(KEY_CURSOR_DESCRIPTOR, null);
            }
            return ret;
        }
    }
}
