package org.devtcg.sqliteserver.impl.binder.protocol;

import android.content.ContentValues;
import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

public class InsertCommand {
    private static final String KEY_TABLE = "table";
    private static final String KEY_VALUES = "values";

    private static final String KEY_ID = "id";

    public static class InsertMessage extends AbstractCommandMessage {
        private final String mTable;
        private final ContentValues mValues;

        private long mInsertId;

        public InsertMessage(
                ClientTransactor transactor,
                String table,
                ContentValues values) {
            super(transactor, MethodName.INSERT);
            mTable = table;
            mValues = values;
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString(KEY_TABLE, mTable);
            request.putParcelable(KEY_VALUES, mValues);
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            mInsertId = response.getLong(KEY_ID);
        }

        public long getInsertId() {
            return mInsertId;
        }
    }

    public static class InsertHandler extends AbstractCommandHandler {
        public InsertHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            String table = request.getString(KEY_TABLE);
            ContentValues values = request.getParcelable(KEY_VALUES);
            long id = getExecutor().insert(table, values);
            Bundle ret = new Bundle();
            ret.putLong(KEY_ID, id);
            return ret;
        }
    }
}
