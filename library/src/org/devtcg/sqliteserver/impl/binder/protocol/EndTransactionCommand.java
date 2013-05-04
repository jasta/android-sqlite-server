package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

public class EndTransactionCommand {
    public static class EndTransactionMessage extends AbstractCommandMessage {
        public EndTransactionMessage(ClientTransactor transactor) {
            super(transactor, MethodName.END_TRANSACTION);
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            // Nothing to do...
        }
    }

    public static class EndTransactionHandler extends AbstractCommandHandler {
        public EndTransactionHandler(SQLiteExecutor executor) {
            super(executor);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            getExecutor().endTransaction();
            return null;
        }
    }
}
