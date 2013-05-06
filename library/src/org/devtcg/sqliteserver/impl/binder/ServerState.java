package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import static android.os.IBinder.DeathRecipient;

public class ServerState {
    /**
     * Client that set up this server state.
     */
    public BinderHandle clientHandle;

    /**
     * Observer for if the client suddenly crashes.  This lets us clean up transactions and
     * other relevant state.
     */
    public DeathRecipient deathRecipient;

    /**
     * All requests from this client will be sent to the same thread because SQLiteDatabase
     * uses thread locals to store state.
     */
    public ThreadAffinityExecutor<Bundle> executor;

    /**
     * Counter for the number of active transactions the client has initiated.
     */
    public int numTransactions;
}
