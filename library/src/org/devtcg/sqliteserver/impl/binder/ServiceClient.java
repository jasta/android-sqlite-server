package org.devtcg.sqliteserver.impl.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import org.devtcg.sqliteserver.impl.binder.protocol.AbstractCommandMessage;
import org.devtcg.sqliteserver.impl.binder.protocol.MethodName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ServiceClient extends AbstractBinderClient {
    private static final String TAG = ServiceClient.class.getSimpleName();

    private final Context mContext;
    private final Intent mServiceIntent;

    private CountDownLatch mConnectLatch;
    private final Object mServiceLock = new Object();
    private IServiceInterface mService;

    public ServiceClient(Context context, Intent serviceIntent) {
        mContext = context;
        mServiceIntent = serviceIntent;
    }

    @Override
    public void close() {
        // See SQLiteServiceServer#onDestroy
        mContext.unbindService(mServiceConnection);

        // TODO: This isn't right, but I'm too sleepy to worry about it now...
        synchronized (mServiceLock) {
            if (mService != null && mConnectLatch.getCount() > 0) {
                mConnectLatch.countDown();
            }
        }
    }

    @Override
    protected Bundle doTransact(Bundle request) {
        // Wait forever, just like we would for a ContentProvider.
        // TODO: verify this absurd statement :)
        waitForConnection(Integer.MAX_VALUE, TimeUnit.SECONDS);
        try {
            return mService.sqliteCall(request);
        } catch (RemoteException e) {
            throw new UnsupportedOperationException("Exceptions not yet supported!");
        }
    }

    private void waitForConnection(long timeout, TimeUnit unit) {
        synchronized (mServiceLock) {
            if (mService != null) {
                return;
            }
        }
        mConnectLatch = new CountDownLatch(1);
        boolean bound = mContext.bindService(
                mServiceIntent,
                mServiceConnection,
                Context.BIND_AUTO_CREATE);
        if (!bound) {
            throw new UnsupportedOperationException("Exceptions not yet supported!");
        }

        while (true) {
            try {
                if (mConnectLatch.await(timeout, unit)) {
                    return;
                }
            } catch (InterruptedException e) {
                // XXX: Implement this correctly :)
            }
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (mServiceLock) {
                mService = IServiceInterface.Stub.asInterface(service);
            }
            mConnectLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service " + name + " unexpectedly disconnected");
            mService = null;
        }
    };
}
