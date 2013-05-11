package org.devtcg.sqliteserver.tests.support;

public class SeparateProcessService extends AbstractTestService {
    @Override
    public boolean isExpectedInMainProcess() {
        return false;
    }
}
