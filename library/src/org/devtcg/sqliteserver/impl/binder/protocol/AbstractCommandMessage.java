package org.devtcg.sqliteserver.impl.binder.protocol;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;
import org.devtcg.sqliteserver.impl.binder.SQLiteServerProtocolException;

/**
 * Client representation of the RPC call (represents a request and its response
 * from the server).
 * <p>
 * TODO: Reimplement commands using an auto-generated aidl interface to avoid the need to
 * allocate a Bundle.
 */
public abstract class AbstractCommandMessage {
    public static final String KEY_METHOD_NAME = "method_name";
    public static final String KEY_CLIENT_BINDER = "client_binder";

    public static final String KEY_SERVER_EXCEPTION_HELPER = "server_exception_helper";

    private final ClientTransactor mTransactor;
    private final MethodName mMethodName;

    private Bundle mRequest;
    private Bundle mResponse;

    public AbstractCommandMessage(ClientTransactor transactor, MethodName methodName) {
        mTransactor = transactor;
        mMethodName = methodName;
    }

    public void transact() throws SQLiteException {
        mRequest = createRequest();
        mRequest = onBuildRequest(mRequest);
        mResponse = mTransactor.transact(mRequest);
        interpretResponse();
    }

    private void handleErrorIfApplicable() throws SQLiteException {
        if (mResponse.containsKey(KEY_SERVER_EXCEPTION_HELPER)) {
            // Throws in this method for remote processes...
            ExceptionTransportHelper exceptionHelper =
                    mResponse.getParcelable(KEY_SERVER_EXCEPTION_HELPER);

            // And here for local processes...
            exceptionHelper.propagateLocalException();
            throw new AssertionError("Must not reach here...");
        }
    }

    private void interpretResponse() throws SQLiteException {
        handleErrorIfApplicable();
        try {
            onParseResponse(mResponse);
        } catch (SQLiteServerProtocolException e) {
            throw new SQLiteException("SQLiteServer error", e);
        }
    }

    protected final Bundle getResponse() {
        return mResponse;
    }

    protected Bundle createRequest() {
        Bundle request = new Bundle();
        request.putInt(KEY_METHOD_NAME, mMethodName.ordinal());
        request.putParcelable(KEY_CLIENT_BINDER, mTransactor.getClientHandle());
        return request;
    }

    protected abstract Bundle onBuildRequest(Bundle request);
    protected abstract void onParseResponse(Bundle response)
            throws SQLiteServerProtocolException;
}
