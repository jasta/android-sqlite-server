package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

/**
 * Client representation of the RPC call (represents a request and its response
 * from the server).
 */
public abstract class AbstractCommandMessage {
    public static final String KEY_METHOD_NAME = "method_name";

    private final ClientTransactor mTransactor;
    private final MethodName mMethodName;

    private Bundle mRequest;
    private Bundle mResponse;

    public AbstractCommandMessage(ClientTransactor transactor, MethodName methodName) {
        mTransactor = transactor;
        mMethodName = methodName;
    }

    public void transact() {
        mRequest = createRequest();
        mRequest = onBuildRequest(mRequest);
        mResponse = mTransactor.transact(mRequest);
        interpretResponse();
    }

    private void handleErrorIfApplicable() {
        // TODO
    }

    private void interpretResponse() {
        handleErrorIfApplicable();
        onParseResponse(mResponse);
    }

    protected final Bundle getResponse() {
        return mResponse;
    }

    protected Bundle createRequest() {
        Bundle request = new Bundle();
        request.putInt(KEY_METHOD_NAME, mMethodName.ordinal());
        return request;
    }

    protected abstract Bundle onBuildRequest(Bundle request);
    protected abstract void onParseResponse(Bundle response);
}
