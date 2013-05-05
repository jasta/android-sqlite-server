package org.devtcg.sqliteserver.impl.binder.protocol;

import android.os.Bundle;
import junit.framework.TestCase;
import org.devtcg.sqliteserver.impl.binder.ClientTransactor;

public class SimpleCommandMessageTest extends TestCase {
    public void testTransactionBundle() {
        TestTransactor transactor = new TestTransactor();
        SimpleCommandMessage message = new SimpleCommandMessage(transactor);
        message.transact();

        assertEquals("foo", transactor.getTestRequestValue());
        assertEquals("bar", message.getTestResultValue());
    }

    private static class SimpleCommandMessage extends AbstractCommandMessage {
        private String mTestResult;

        public SimpleCommandMessage(ClientTransactor transactor) {
            super(transactor, MethodName.INSERT /* dummy */);
        }

        @Override
        protected Bundle onBuildRequest(Bundle request) {
            request.putString("test_request_key", "foo");
            return request;
        }

        @Override
        protected void onParseResponse(Bundle response) {
            mTestResult = response.getString("test_result_key");
        }

        public String getTestResultValue() {
            return mTestResult;
        }
    }

    private static class TestTransactor implements ClientTransactor {
        private String mTestRequestValue;

        @Override
        public Bundle transact(Bundle request) {
            mTestRequestValue = request.getString("test_request_key");
            Bundle ret = new Bundle();
            ret.putString("test_result_key", "bar");
            return ret;
        }

        public String getTestRequestValue() {
            return mTestRequestValue;
        }
    }
}
