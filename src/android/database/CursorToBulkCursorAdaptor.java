/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.database;

import android.os.Bundle;
import android.os.IBinder;


public final class CursorToBulkCursorAdaptor extends BulkCursorNative
        implements IBinder.DeathRecipient {
    public CursorToBulkCursorAdaptor(Cursor cursor, IContentObserver observer,
            String providerName) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void binderDied() {
        throw new RuntimeException("Stub!");
    }

    public BulkCursorDescriptor getBulkCursorDescriptor() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public CursorWindow getWindow(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void onMove(int position) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void deactivate() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public void close() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int requery(IContentObserver observer) {
        throw new RuntimeException("Stub!");
    }

    @Override
    public Bundle getExtras() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public Bundle respond(Bundle extras) {
        throw new RuntimeException("Stub!");
    }
}
