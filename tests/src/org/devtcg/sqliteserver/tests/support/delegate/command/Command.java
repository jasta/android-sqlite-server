package org.devtcg.sqliteserver.tests.support.delegate.command;

import android.os.Parcelable;
import org.devtcg.sqliteserver.tests.support.delegate.DelegateState;

import java.io.IOException;

public interface Command extends Parcelable {
    /**
     * Execute the command (typically this is performing a
     * {@link org.devtcg.sqliteserver.SQLiteServerConnection} call.
     */
    public void execute(DelegateState state);
}
