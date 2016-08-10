package com.silencedut.androidipc;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.content.Context.ACTIVITY_SERVICE;

public class MainActivity extends AppCompatActivity {
    IStudyBinder mStudyBinder;
    private TextView mResultTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultTv = (TextView)findViewById(R.id.result_tv);
        Intent intent = new Intent(this,StudyBinderService.class);
        bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
    }

    private ServiceConnection mServiceConn = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            Log.e("client", "onServiceDisconnected");

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            Log.e("client", "onServiceConnected");
            mStudyBinder = IStudyBinder.Stub.asInterface(service);
            try {
                mResultTv.setText(mStudyBinder.studyBinder("SilenceDut"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
}
