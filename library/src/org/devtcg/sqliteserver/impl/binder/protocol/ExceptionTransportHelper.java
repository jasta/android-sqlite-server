package org.devtcg.sqliteserver.impl.binder.protocol;

import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;

public class ExceptionTransportHelper implements Parcelable {
    private final RuntimeException mException;

    public ExceptionTransportHelper(RuntimeException exception) {
        mException = exception;
    }

    private ExceptionTransportHelper(Parcel in) throws RuntimeException {
        // Watch out...
        throw readExceptionAndThrow(in);
    }

    private static RuntimeException readExceptionAndThrow(Parcel in) throws RuntimeException {
        DatabaseUtils.readExceptionFromParcel(in);
        throw new RuntimeException("Inconsistent remote reply; did not throw as expected");
    }

    public void propagateLocalException() throws SQLiteException {
        if (mException == null) {
            throw new RuntimeException("Inconsistent local reply; did not throw as expected");
        }
        throw mException;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // This can throw RuntimeException if mException is not parcelable.
        DatabaseUtils.writeExceptionToParcel(dest, mException);
    }

    public static final Creator<ExceptionTransportHelper> CREATOR =
            new Creator<ExceptionTransportHelper>() {
        @Override
        public ExceptionTransportHelper createFromParcel(Parcel source) {
            return new ExceptionTransportHelper(source);
        }

        @Override
        public ExceptionTransportHelper[] newArray(int size) {
            return new ExceptionTransportHelper[size];
        }
    };
}
