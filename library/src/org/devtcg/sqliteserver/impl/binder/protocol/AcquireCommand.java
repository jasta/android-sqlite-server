package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.*;
import org.devtcg.sqliteserver.impl.binder.*;

public class AcquireCommand {
    private static final String KEY_SERVER_BINDER = "server_binder";

    public static final class AcquireMessage extends AbstractCommandMessage {
        private BinderHandle mServerHandle;

        public AcquireMessage(ClientTransactor transactor) {
            super(transactor, MethodName.ACQUIRE);
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            // AbstractCommandMessage already handles putting the client handle on
            // the request...
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) throws SQLiteServerProtocolException {
            mServerHandle = BundleUtils.getParcelableOrThrow(response, KEY_SERVER_BINDER);
        }

        public BinderHandle getServerHandle() {
            return mServerHandle;
        }
    }

    public static final class AcquireHandler extends AbstractCommandHandler {
        public AcquireHandler(ServerImpl serverImpl) {
            super(serverImpl);
        }

        @Override
        protected Bundle onHandle(Bundle request) throws SQLiteServerProtocolException {
            try {
                getServerImpl().initServerState(getClientHandle());
            } catch (RemoteException e) {
                // Hehe, technically this is a protocol exception :)
                throw new SQLiteServerProtocolException("Client is not supposed to die", e);
            }

            Bundle ret = new Bundle();
            ret.putParcelable(KEY_SERVER_BINDER, new BinderHandle());
            return ret;
        }
    }

}
