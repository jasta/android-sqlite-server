package aosp.android.database;

import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Build;

/**
 * Copied critical hidden APIs from Android's DatabaseUtils.
 */
class MoreDatabaseUtils {
    /**
     * Fills the specified cursor window by iterating over the contents of the cursor.
     * The window is filled until the cursor is exhausted or the window runs out
     * of space.
     *
     * The original position of the cursor is left unchanged by this operation.
     *
     * @param cursor The cursor that contains the data to put in the window.
     * @param position The start position for filling the window.
     * @param window The window to fill.
     * @hide
     */
    public static void cursorFillWindow(final Cursor cursor,
            int position, final CursorWindow window) {
        if (position < 0 || position >= cursor.getCount()) {
            return;
        }
        final int oldPos = cursor.getPosition();
        final int numColumns = cursor.getColumnCount();
        window.clear();
        window.setStartPosition(position);
        window.setNumColumns(numColumns);
        if (cursor.moveToPosition(position)) {
            do {
                if (!window.allocRow()) {
                    break;
                }
                for (int i = 0; i < numColumns; i++) {
                    boolean success = putToWindow(cursor, i, window, position);
                    if (!success) {
                        window.freeLastRow();
                        break;
                    }
                }
                position += 1;
            } while (cursor.moveToNext());
        }
        cursor.moveToPosition(oldPos);
    }

    /**
     * This method was introduced by AndroidSQLiteServer to work around the lack of
     * {@link Cursor#getType(int)}} on Gingerbread.
     */
    private static boolean putToWindow(Cursor cursor, int column,
            CursorWindow window, int row) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return putToWindowHoneycombAndBeyond(cursor, column, window, row);
        } else {
            return putToWindowPreHoneycomb(cursor, column, window, row);
        }
    }

    private static boolean putToWindowHoneycombAndBeyond(Cursor cursor, int i,
            CursorWindow window, int position) {
        final int type = cursor.getType(i);
        final boolean success;
        switch (type) {
            case Cursor.FIELD_TYPE_NULL:
                success = window.putNull(position, i);
                break;

            case Cursor.FIELD_TYPE_INTEGER:
                success = window.putLong(cursor.getLong(i), position, i);
                break;

            case Cursor.FIELD_TYPE_FLOAT:
                success = window.putDouble(cursor.getDouble(i), position, i);
                break;

            case Cursor.FIELD_TYPE_BLOB: {
                final byte[] value = cursor.getBlob(i);
                success = value != null ? window.putBlob(value, position, i)
                        : window.putNull(position, i);
                break;
            }

            default: // assume value is convertible to String
            case Cursor.FIELD_TYPE_STRING: {
                final String value = cursor.getString(i);
                success = value != null ? window.putString(value, position, i)
                        : window.putNull(position, i);
                break;
            }
        }

        return success;
    }

    /**
     * This solution is consistent with how Gingerbread implemented
     * {@link android.database.AbstractCursor#fillWindow(int, android.database.CursorWindow)}.
     */
    private static boolean putToWindowPreHoneycomb(Cursor cursor, int i,
            CursorWindow window, int position) {
        String value = cursor.getString(i);
        final boolean success;
        if (value != null) {
            success = window.putString(value, position, i);
        } else {
            success = window.putNull(position, i);
        }
        return success;
    }

    public static int findRowIdColumnIndex(String[] columnNames) {
        int length = columnNames.length;
        for (int i = 0; i < length; i++) {
            if (columnNames[i].equals("_id")) {
                return i;
            }
        }
        return -1;
    }
}