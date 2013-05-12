package org.devtcg.sqliteserver.tests;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;
import android.util.Log;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.tests.util.ConnectionProvider;
import org.devtcg.sqliteserver.tests.util.ConnectionProviders;
import org.devtcg.sqliteserver.tests.util.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Simple tests that exercise large portions of the engine under a multi-threaded scenario.
 */
public class ThreadedTest extends AndroidTestCase {
    /**
     * Tests SQLiteServer with multiple connections, one per thread.
     */
    public void testMultiConnectionSupport() {
        for (ConnectionProvider provider : ConnectionProviders.allConnections(getContext())) {
            SharedThreadState state = new SharedThreadState();
            List<AbstractTestThread> threads = new ArrayList<AbstractTestThread>();
            threads.add(new MasterTestThread(state, provider));
            threads.add(new SlaveTestThread(state, provider));

            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                joinUninterruptibly(thread);
            }

            if (!state.failures.isEmpty()) {
                fail(state.failures.get(0));
            }
        }
    }

    private static class SharedThreadState {
        public volatile CountDownLatch currentLatch = new CountDownLatch(1);
        public List<String> failures = Collections.synchronizedList(new ArrayList<String>());
    }

    private static abstract class AbstractTestThread extends Thread {
        private final ConnectionProvider mProvider;
        private final SharedThreadState mThreadState;

        public AbstractTestThread(SharedThreadState threadState,
                ConnectionProvider provider) {
            super();
            setName(getClass().getSimpleName());
            mThreadState = threadState;
            mProvider = provider;
        }

        protected final SQLiteServerConnection openConnection() {
            return mProvider.openConnection();
        }

        public void recordFailure(String message) {
            mThreadState.failures.add(getName() + " reports failure: " + message);
        }

        public void run() {
            try {
                doRun();
            } catch (SQLiteException e) {
                recordFailure("Unexpected SQLiteException: " + e);
            } finally {
                /* This is only needed by the master to inform the slave that the last
                 * handoff has completed... */
                synchronized (mThreadState) {
                    mThreadState.currentLatch.countDown();
                }
            }
        }

        protected abstract void doRun() throws SQLiteException;

        public void handoff() {
            CountDownLatch nextLatch = new CountDownLatch(1);

            synchronized (mThreadState) {
                mThreadState.currentLatch.countDown();
                mThreadState.currentLatch = nextLatch;
            }

            waitForPeer(nextLatch);
        }

        public void waitForHandoff() {
            waitForPeer(mThreadState.currentLatch);
        }

        private void waitForPeer(CountDownLatch latch) {
            Log.d(getName(), "Waiting on other thread...");
            awaitUninterruptibly(latch);
            Log.d(getName(), "Control returned.");
        }
    }

    private static class MasterTestThread extends AbstractTestThread {
        public MasterTestThread(SharedThreadState state, ConnectionProvider provider) {
            super(state, provider);
        }

        public void doRun() throws SQLiteException {
            SQLiteServerConnection conn = openConnection();

            try {
                conn.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put("test1", "foo");
                    values.put("test2", "bar");
                    conn.insert("test", values);

                    values.put("test2", "baz");
                    conn.insert("test", values);

                    conn.setTransactionSuccessful();
                } finally {
                    conn.endTransaction();
                }

                handoff();

                if (!DbHelper.isEmpty(conn.rawQuery("SELECT * FROM test", new String[] {}))) {
                    recordFailure("Slave did not delete rows as expected");
                }
            } finally {
                conn.close();
            }
        }
    }

    private static class SlaveTestThread extends AbstractTestThread {
        public SlaveTestThread(SharedThreadState state, ConnectionProvider provider) {
            super(state, provider);
        }

        public void doRun() throws SQLiteException {
            waitForHandoff();

            SQLiteServerConnection conn = openConnection();

            try {
                if (DbHelper.isEmpty(conn.rawQuery("SELECT * FROM test", new String[]{}))) {
                    recordFailure("Master did not insert records as expected");
                }

                conn.delete("test", null, null);

                handoff();
            } finally {
                conn.close();
            }
        }
    }

    public static void joinUninterruptibly(Thread t) {
        while (true) {
            try {
                t.join();
                return;
            } catch (InterruptedException e) {
                // Ignore...
            }
        }
    }

    public static void awaitUninterruptibly(CountDownLatch latch) {
        while (true) {
            try {
                latch.await();
                return;
            } catch (InterruptedException e) {
                // Ignore...
            }
        }
    }
}
