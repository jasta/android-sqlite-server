package org.devtcg.sqliteserver;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import org.devtcg.sqliteserver.impl.ServerImplProvider;
import org.devtcg.sqliteserver.impl.binder.IServiceInterface;
import org.devtcg.sqliteserver.impl.binder.ServerImpl;

import java.lang.ref.WeakReference;

public abstract class SQLiteServiceServer extends Service implements SQLiteServer {
    private ServerImplProvider mServerImplProvider;

    private final IBinder mBinder = new ServiceStub(this);

    @Override
    public void onCreate() {
        super.onCreate();
        mServerImplProvider = new ServerImplProvider(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mServerImplProvider.get().closeDatabase();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private static class ServiceStub extends IServiceInterface.Stub {
        // See https://code.google.com/p/android/issues/detail?id=6426
        private final WeakReference<SQLiteServiceServer> mService;

        public ServiceStub(SQLiteServiceServer service) {
            mService = new WeakReference<SQLiteServiceServer>(service);
        }

        @Override
        public Bundle sqliteCall(Bundle request) throws RemoteException {
            SQLiteServiceServer service = mService.get();
            ServerImpl serverImpl = service.mServerImplProvider.get();
            return serverImpl.onTransact(request);
        }
    }
}
