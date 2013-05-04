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
import android.os.IInterface;
import android.os.RemoteException;

public interface IBulkCursor extends IInterface  {
    public CursorWindow getWindow(int position) throws RemoteException;

    public void onMove(int position) throws RemoteException;

    public void deactivate() throws RemoteException;

    public void close() throws RemoteException;

    public int requery(IContentObserver observer) throws RemoteException;

    Bundle getExtras() throws RemoteException;

    Bundle respond(Bundle extras) throws RemoteException;
}
