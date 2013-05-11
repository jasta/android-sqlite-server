package org.devtcg.sqliteserver.tests.support.delegate.command;

import android.os.Parcel;
import org.devtcg.sqliteserver.tests.support.delegate.DelegateState;

public class BeginTransactionCommand implements Command {
    public BeginTransactionCommand() {
    }

    private BeginTransactionCommand(Parcel in) {
    }

    @Override
    public void execute(DelegateState state) {
        state.conn.beginTransaction();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Creator<BeginTransactionCommand> CREATOR =
            new Creator<BeginTransactionCommand>() {
        @Override
        public BeginTransactionCommand createFromParcel(Parcel source) {
            return new BeginTransactionCommand(source);
        }

        @Override
        public BeginTransactionCommand[] newArray(int size) {
            return new BeginTransactionCommand[size];
        }
    };
}
