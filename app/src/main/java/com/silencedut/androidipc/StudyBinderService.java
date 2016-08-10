package com.silencedut.androidipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by SilenceDut on 16/8/10.
 */

public class StudyBinderService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IStudyBinder.Stub mBinder= new IStudyBinder.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String studyBinder(String name) throws RemoteException {

            return name+"studyBinder";
        }
    };
}
