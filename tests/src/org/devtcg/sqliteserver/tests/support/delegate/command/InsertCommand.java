package org.devtcg.sqliteserver.tests.support.delegate.command;

import android.content.ContentValues;
import android.os.Parcel;
import org.devtcg.sqliteserver.tests.support.delegate.DelegateState;

public class InsertCommand implements Command {
    private final String mTable;
    private final ContentValues mValues;

    public InsertCommand(String table, ContentValues values) {
        mTable = table;
        mValues = values;
    }

    private InsertCommand(Parcel in) {
        mTable = in.readString();
        mValues = in.readParcelable(getClass().getClassLoader());
    }

    @Override
    public void execute(DelegateState state) {
        state.conn.insert(mTable, mValues);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTable);
        dest.writeParcelable(mValues, flags);
    }

    public static final Creator<InsertCommand> CREATOR = new Creator<InsertCommand>() {
        @Override
        public InsertCommand createFromParcel(Parcel source) {
            return new InsertCommand(source);
        }

        @Override
        public InsertCommand[] newArray(int size) {
            return new InsertCommand[size];
        }
    };
}
