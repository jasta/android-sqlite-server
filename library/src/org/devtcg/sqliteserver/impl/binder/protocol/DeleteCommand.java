package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

public class DeleteCommand {
    private static final String KEY_TABLE = "table";
    private static final String KEY_WHERE_CLAUSE = "where_clause";
    private static final String KEY_WHERE_ARGS = "where_args";

    private static final String KEY_COUNT = "count";

    public static class DeleteMessage extends AbstractCommandMessage {
        private final String mTable;
        private final String mWhereClause;
        private final String[] mWhereArgs;

        private int mAffectedRowsCount;

        public DeleteMessage(
                ClientTransactor transactor,
                String table,
                String whereClause,
                String[] whereArgs) {
            super(transactor, MethodName.DELETE);
            mTable = table;
            mWhereClause = whereClause;
            mWhereArgs = whereArgs;
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString(KEY_TABLE, mTable);
            request.putString(KEY_WHERE_CLAUSE, mWhereClause);
            request.putStringArray(KEY_WHERE_ARGS, mWhereArgs);
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            mAffectedRowsCount = response.getInt(KEY_COUNT);
        }

        public int getAffectedRowsCount() {
            return mAffectedRowsCount;
        }
    }

    public static class DeleteHandler extends AbstractCommandHandler {
        public DeleteHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            String table = request.getString(KEY_TABLE);
            String whereClause = request.getString(KEY_WHERE_CLAUSE);
            String[] whereArgs = request.getStringArray(KEY_WHERE_ARGS);
            int count = getExecutor().delete(table, whereClause, whereArgs);
            Bundle ret = new Bundle();
            ret.putInt(KEY_COUNT, count);
            return ret;
        }
    }
}
