package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

public class ExecSQLCommand {
    private static final String KEY_SQL = "sql";

    public static class ExecSQLMessage extends AbstractCommandMessage {
        private final String mSql;

        public ExecSQLMessage(ClientTransactor transactor, String sql) {
            super(transactor, MethodName.EXEC_SQL);
            mSql = sql;
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString(KEY_SQL, mSql);
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            // Nothing to do...
        }
    }

    public static class ExecSQLHandler extends AbstractCommandHandler {
        public ExecSQLHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            String sql = request.getString(KEY_SQL);
            getExecutor().execSQL(sql);
            return null;
        }
    }
}
