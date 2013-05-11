package org.devtcg.sqliteserver.tests.support;

public class SeparateProcessContentProvider extends AbstractTestContentProvider {
    public static final String AUTHORITY = SeparateProcessContentProvider.class.getName();

    @Override
    public boolean isExpectedInMainProcess() {
        return false;
    }
}
