package org.devtcg.sqliteserver.impl.binder;

import android.os.Bundle;
import android.os.Parcelable;

public class BundleUtils {
    private static void ensureContainsKey(Bundle b, String key)
            throws SQLiteServerProtocolException {
        if (!b.containsKey(key)) {
            throw new SQLiteServerProtocolException("Missing required key: " + key);
        }
    }

    public static String getStringOrThrow(Bundle b, String key)
            throws SQLiteServerProtocolException {
        ensureContainsKey(b, key);
        return b.getString(key);
    }

    public static int getIntOrThrow(Bundle b, String key)
            throws SQLiteServerProtocolException {
        ensureContainsKey(b, key);
        return b.getInt(key);
    }

    public static <T extends Parcelable> T getParcelableOrThrow(Bundle b, String key)
            throws SQLiteServerProtocolException {
        ensureContainsKey(b, key);
        return (T)b.getParcelable(key);
    }
}
