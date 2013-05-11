package org.devtcg.sqliteserver.tests.support.delegate.command;

import android.content.Intent;
import android.os.Parcel;
import org.devtcg.sqliteserver.SQLiteServerConnection;
import org.devtcg.sqliteserver.SQLiteServerConnectionManager;
import org.devtcg.sqliteserver.tests.support.delegate.DelegateState;
import org.devtcg.sqliteserver.tests.util.ConnectionProvider;

public class OpenConnectionCommand implements Command {
    private final String mAuthority;
    private final Intent mIntent;

    public OpenConnectionCommand(String authority) {
        mAuthority = authority;
        mIntent = null;
    }

    public OpenConnectionCommand(Intent intent) {
        mAuthority = null;
        mIntent = intent;
    }

    private OpenConnectionCommand(Parcel in) {
        mAuthority = in.readString();
        mIntent = in.readParcelable(getClass().getClassLoader());
    }

    @Override
    public void execute(DelegateState state) {
        SQLiteServerConnectionManager connMgr =
                new SQLiteServerConnectionManager(state.context);
        SQLiteServerConnection conn;
        if (mAuthority != null) {
            conn = connMgr.openConnectionToContentProvider(mAuthority);
        } else if (mIntent != null) {
            conn = connMgr.openConnectionToService(mIntent);
        } else {
            throw new IllegalArgumentException("What, no authority or intent?");
        }
        state.conn = conn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthority);
        dest.writeParcelable(mIntent, flags);
    }

    public static final Creator<OpenConnectionCommand> CREATOR =
            new Creator<OpenConnectionCommand>() {
        @Override
        public OpenConnectionCommand createFromParcel(Parcel source) {
            return new OpenConnectionCommand(source);
        }

        @Override
        public OpenConnectionCommand[] newArray(int size) {
            return new OpenConnectionCommand[size];
        }
    };
}
