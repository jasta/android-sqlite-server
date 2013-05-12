package org.devtcg.sqliteserver.tests.support;

import android.database.sqlite.SQLiteDatabase;
import org.devtcg.sqliteserver.SQLiteContentProviderServer;
import org.devtcg.sqliteserver.tests.util.ProcessHelper;

import static junit.framework.Assert.assertEquals;

abstract class AbstractTestContentProvider extends SQLiteContentProviderServer
        implements ProcessChecker {
    @Override
    public SQLiteDatabase getWritableDatabase() {
        assertEquals(isExpectedInMainProcess(), ProcessHelper.inMainProcess(getContext()));
        return new TestDatabaseOpenHelper(getContext(), getClass().getSimpleName())
                .getWritableDatabase();
    }

    @Override
    public String getServerName() {
        return getClass().getName();
    }
}
