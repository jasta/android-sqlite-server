package org.devtcg.sqliteserver.impl.binder;

import android.database.IContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

/**
 * This class modeled after android-4.2.2_r1's
 * frameworks/base/core/java/android/content/ContentProviderNative.java.
 * <p>
 * This component is a proxy that allows both sides (client and server) of a Cursor-yielding
 * operation to communicate information about the underlying data set. This is what powers
 * Android's {@link android.database.Cursor#registerContentObserver} feature.
 */
public class ContentObserverProxy implements IContentObserver, Parcelable {
    private final IContentObserver mRemote;

    public ContentObserverProxy(IContentObserver remote) {
        mRemote = remote;
    }

    public ContentObserverProxy(Parcel in) {
        mRemote = Stub.asInterface(in.readStrongBinder());
    }

    @Override
    public void onChange(boolean selfUpdate, Uri uri) throws RemoteException {
        mRemote.onChange(selfUpdate, uri);
    }

    @Override
    public IBinder asBinder() {
        return mRemote.asBinder();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(mRemote.asBinder());
    }

    @SuppressWarnings("unused")
    public static final Creator<ContentObserverProxy> CREATOR =
            new Creator<ContentObserverProxy>() {
        @Override
        public ContentObserverProxy createFromParcel(Parcel source) {
            return new ContentObserverProxy(source);
        }

        @Override
        public ContentObserverProxy[] newArray(int size) {
            return new ContentObserverProxy[size];
        }
    };
}
