package org.devtcg.sqliteserver.tests.util;

import android.database.Cursor;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class DbHelper {
    public static boolean isEmpty(Cursor cursor) {
        try {
            return cursor.getCount() == 0;
        } finally {
            cursor.close();
        }
    }

    public static void assertEmpty(Cursor cursor) {
        assertTrue(isEmpty(cursor));
    }

    public static void assertNotEmpty(Cursor cursor) {
        assertFalse(isEmpty(cursor));
    }
}
