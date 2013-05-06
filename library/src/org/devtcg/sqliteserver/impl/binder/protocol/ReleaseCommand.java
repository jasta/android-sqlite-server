package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.SQLiteServerProtocolException;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;
import org.devtcg.sqliteserver.impl.binder.ServerState;

public class ReleaseCommand {
    public static class ReleaseMessage extends AbstractCommandMessage {
        public ReleaseMessage(ClientTransactor transactor) {
            super(transactor, MethodName.RELEASE);
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
        }
    }

    public static class ReleaseHandler extends AbstractCommandHandler {
        public ReleaseHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) throws SQLiteServerProtocolException {
            getServerImpl().performRelease(getServerState());
            return null;
        }
    }
}
