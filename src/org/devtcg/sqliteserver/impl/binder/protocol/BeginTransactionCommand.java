package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

public class BeginTransactionCommand {
    public static class BeginTransactionMessage extends AbstractCommandMessage {
        public BeginTransactionMessage(ClientTransactor transactor) {
            super(transactor, MethodName.BEGIN_TRANSACTION);
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            // Nothing to do.
        }
    }

    public static class BeginTransactionHandler extends AbstractCommandHandler {
        public BeginTransactionHandler(SQLiteExecutor executor) {
            super(executor);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            getExecutor().beginTransaction();
            return null;
        }
    }
}