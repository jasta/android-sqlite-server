package org.devtcg.sqliteserver.impl;

import android.os.HandlerThread;
import org.devtcg.sqliteserver.impl.binder.ThreadAffinityExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorHelper {
    private static final AtomicInteger mCount = new AtomicInteger(1);

    private ExecutorHelper() {}

    public static <T> ThreadAffinityExecutor<T> createThreadAffinityExecutor() {
        String threadName = "ThreadAffinityExec-" + mCount.getAndIncrement();
        return createThreadAffinityExecutor(threadName);
    }

    public static <T> ThreadAffinityExecutor<T> createThreadAffinityExecutor(String threadName) {
        HandlerThread thread = new HandlerThread(threadName);
        thread.start();
        return new ThreadAffinityExecutor<T>(thread.getLooper());
    }
}
