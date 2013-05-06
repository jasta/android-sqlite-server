package org.devtcg.sqliteserver.impl.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO: Convert this and all of the rest of the Bundle goofiness to an aidl generated
 * Stub/Proxy design.  It's much easier to understand, and can be much better optimized
 * as well (we don't need to encode things into Bundle's in all cases).
 */
public class BinderHandle implements Parcelable {
    private final IBinder mBinder;

    /**
     * Construct a local binder handle to be transmitted to a remote peer.
     */
    public BinderHandle() {
        mBinder = new Binder();
    }

    /**
     * Construct a binder handle representative of the local binder handle
     * transmitted to us by a remote peer.
     */
    public BinderHandle(Parcel in) {
        mBinder = in.readStrongBinder();
    }

    public IBinder asBinder() {
        return mBinder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(mBinder);
    }

    @SuppressWarnings("unused")
    public static final Creator<BinderHandle> CREATOR = new Creator<BinderHandle>() {
        @Override
        public BinderHandle createFromParcel(Parcel source) {
            return new BinderHandle(source);
        }

        @Override
        public BinderHandle[] newArray(int size) {
            return new BinderHandle[size];
        }
    };
}
