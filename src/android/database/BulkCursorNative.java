package android.database;

import android.os.Binder;
import android.os.IBinder;

public abstract class BulkCursorNative extends Binder implements IBulkCursor {
    public IBinder asBinder() {
        throw new RuntimeException("Stub!");
    }
}
