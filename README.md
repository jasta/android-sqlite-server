# AndroidSQLiteServer - Rich IPC friendly SQLite API for Android.

AndroidSQLiteServer solves a common problem developers face when they need the
full flexibility and API simplicity of SQLiteDatabase but where it is necessary
to use a ContentProvider for multiprocess / IPC purposes.

This package provides a drop-in API replacement for SQLiteDatabase but that can
connect to a separate process to execute the database operations.  Better
still, you can even connect to Service components instead of ContentProvider's,
giving you the flexibility to have multiple instances of the same database
schema.

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

# Supported Android Versions

The project goal is to support Gingerbread (API level 9) and up.  The current
version, however, only supports Gingerbread for the Service-backed server case
(not the ContentProvider-backed case).

# Contributing

Feel free to fork and improve.  I will do my best to respond to issues and pull
requests!

## TODO

* [important] Gingerbread support for the ContentProvider case would be nice,
  but it isn't straight forward to add.  The major problem is hooking up the
  process death recipients.
* [important] Implement a local transport that just passes through to
  SQLiteExecutor (so, functionally equivalent to how SQLiteDatabase works now).
* [important] Detect same process cases for ContentProvider and Service and
  just invoke methods directly.
* [wishlist] Refactor the very generic AbstractBinderClient such that each
  transport (ContentProvider or Service) can implement its own, more efficient,
  dispatching logic.  For example, Service can define an aidl that parcels the
  messages directly, no longer requiring Bundle.

# License

Apache License, Version 2.0
