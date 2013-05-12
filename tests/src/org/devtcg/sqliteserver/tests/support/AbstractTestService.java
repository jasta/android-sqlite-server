package org.devtcg.sqliteserver.tests.support;

import android.database.sqlite.SQLiteDatabase;
import org.devtcg.sqliteserver.SQLiteServiceServer;
import org.devtcg.sqliteserver.tests.util.ProcessHelper;

import static junit.framework.Assert.assertEquals;

abstract class AbstractTestService extends SQLiteServiceServer
        implements ProcessChecker {
    @Override
    public SQLiteDatabase getWritableDatabase() {
        // XXX: This may crash off the main thread, triggering the whole test suite to
        // fall down and report a crash rather than a test failure.  Not awesome, but at
        // least it's not silently ignored :)
        assertEquals(isExpectedInMainProcess(), ProcessHelper.inMainProcess(this));
        return new TestDatabaseOpenHelper(this, getClass().getSimpleName())
                .getWritableDatabase();
    }

    @Override
    public String getServerName() {
        return getClass().getName();
    }
}
