package org.devtcg.sqliteserver.tests.support;

public class SameProcessContentProvider extends AbstractTestContentProvider {
    public static final String AUTHORITY = SameProcessContentProvider.class.getName();

    @Override
    public boolean isExpectedInMainProcess() {
        return true;
    }
}
