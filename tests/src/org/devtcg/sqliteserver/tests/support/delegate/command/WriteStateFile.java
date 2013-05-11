package org.devtcg.sqliteserver.tests.support.delegate.command;

import android.os.Parcel;
import org.devtcg.sqliteserver.tests.support.delegate.DelegateState;

import java.io.File;
import java.io.IOException;

/**
 * IPC hack (avoiding Android's IPC system) to communicate with the test that the delegate
 * service has entered the desired "stable" state.
 */
public class WriteStateFile implements Command {
    private final File mFile;

    public WriteStateFile(File file) {
        mFile = file;
    }

    private WriteStateFile(Parcel in) {
        mFile = new File(in.readString());
    }

    @Override
    public void execute(DelegateState state) {
        try {
            if (!mFile.createNewFile()) {
                throw new IOException("Failed to create: " + mFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFile.getAbsolutePath());
    }

    public static final Creator<WriteStateFile> CREATOR = new Creator<WriteStateFile>() {
        @Override
        public WriteStateFile createFromParcel(Parcel source) {
            return new WriteStateFile(source);
        }

        @Override
        public WriteStateFile[] newArray(int size) {
            return new WriteStateFile[size];
        }
    };
}
