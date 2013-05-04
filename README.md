# AndroidSQLiteServer - Rich IPC friendly SQLite API for Android.

AndroidSQLite solves a common problem developers face when they need the full
flexibility and API simplicity of SQLiteDatabase but where it is necessary to
use a ContentProvider for multiprocess / IPC purposes.

This package provides a drop-in API replacement for SQLiteDatabase but that can
connect to a separate process to execute the database operations.  Better
still, you can even connect to Service components instead of ContentProvider's,
giving you the flexibility to have multiple instances of the same service.

Best of all, using this library does not mean you can't still provide a
traditional ContentProvider API for your component!

# Sample Usage

```java
SQLiteServerConnectionManager connMgr =
  new SQLiteServerConnectionManager(getApplicationContext());
SQLiteServerConnection conn =
  connMgr.openConnectionToContentProvider(MyContentProvider.AUTHORITY);
try {
    Cursor cursor = conn.rawQuery("SELECT foo FROM testTable", new String[] {});
    try {
        while (cursor.moveToNext()) {
            String foo = cursor.getString(0);
            System.out.println("foo=" + foo);
        }
    } finally {
        cursor.close();
    }
} finally {
    /* Closes the connection handle; not necessarily the database being
     * hosted in MyContentProvider. */
    conn.close();
}
```

# Contributing

Feel free to fork and improve.  I will do my best to respond to issues and pull requests!

# License

Apache License, Version 2.0
