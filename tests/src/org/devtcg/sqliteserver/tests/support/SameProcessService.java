package org.devtcg.sqliteserver.tests.support;

public class SameProcessService extends AbstractTestService {
    @Override
    public boolean isExpectedInMainProcess() {
        return true;
    }
}
