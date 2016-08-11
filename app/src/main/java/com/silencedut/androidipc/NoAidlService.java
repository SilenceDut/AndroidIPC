package com.silencedut.androidipc;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by SilenceDut on 16/8/11.
 */

public class NoAidlService extends Service {
    public static final int TRANSACTION_studyBinder = 0x001;
    private static final String DESCRIPTOR = "NoAidlService";
    private Binder mNoAidlBinder = new NoAidlBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mNoAidlBinder;

    }
    private class NoAidlBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                                     int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_studyBinder: {
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0;
                    _arg0 = data.readString();
                    String _result = _arg0+" Study NoAidlService";
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
