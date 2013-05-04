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

public final class BulkCursorToCursorAdaptor extends AbstractWindowedCursor {
    public void initialize(BulkCursorDescriptor d) {
        throw new RuntimeException("Stub!");
    }

    public IContentObserver getObserver() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public int getCount() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
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
    public boolean requery() {
        throw new RuntimeException("Stub!");
    }

    @Override
    public String[] getColumnNames() {
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
