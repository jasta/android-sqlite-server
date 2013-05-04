package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

public class SetTransactionSuccessfulCommand {
    public static class SetTransactionSuccessfulMessage extends AbstractCommandMessage {
        public SetTransactionSuccessfulMessage(ClientTransactor transactor) {
            super(transactor, MethodName.SET_TRANSACTION_SUCCESSFUL);
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

    public static class SetTransactionSuccessfulHandler extends AbstractCommandHandler {
        public SetTransactionSuccessfulHandler(SQLiteExecutor executor) {
            super(executor);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            getExecutor().setTransactionSuccessful();
            return null;
        }
    }
}
