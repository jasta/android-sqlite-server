package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.SQLiteExecutor;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;
import org.devtcg.sqliteserver.impl.binder.ServerState;

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
        public EndTransactionHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) {
            getExecutor().endTransaction();
            ServerState state = getServerState();
            if (state.numTransactions == 0) {
                throw new IllegalStateException("Expected SQLiteDatabase#endTransaction to throw");
            }
            state.numTransactions--;
            return null;
        }
    }
}
