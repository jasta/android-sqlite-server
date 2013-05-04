package org.devtcg.sqliteserver.impl.binder.protocol;

import android.content.ContentValues;
import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

public class UpdateCommand {
    private static final String KEY_TABLE = "table";
    private static final String KEY_VALUES = "values";
    private static final String KEY_WHERE_CLAUSE = "where_clause";
    private static final String KEY_WHERE_ARGS = "where_args";

    private static final String KEY_COUNT = "count";

    public static class UpdateMessage extends AbstractCommandMessage {
        private final String mTable;
        private final ContentValues mValues;
        private final String mWhereClause;
        private final String[] mWhereArgs;

        private int mAffectedRowsCount;

        public UpdateMessage(
                ClientTransactor transactor,
                String table,
                ContentValues values,
                String whereClause,
                String[] whereArgs) {
            super(transactor, MethodName.UPDATE);
            mTable = table;
            mValues = values;
            mWhereClause = whereClause;
            mWhereArgs = whereArgs;
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString(KEY_TABLE, mTable);
            request.putParcelable(KEY_VALUES, mValues);
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

    public static class UpdateHandler extends AbstractCommandHandler {
        public UpdateHandler(SQLiteExecutor executor) {
            super(executor);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            String table = request.getString(KEY_TABLE);
            ContentValues values = request.getParcelable(KEY_VALUES);
            String whereClause = request.getString(KEY_WHERE_CLAUSE);
            String[] whereArgs = request.getStringArray(KEY_WHERE_ARGS);
            int count = getExecutor().update(table, values, whereClause, whereArgs);
            Bundle ret = new Bundle();
            ret.putInt(KEY_COUNT, count);
            return ret;
        }
    }
}
