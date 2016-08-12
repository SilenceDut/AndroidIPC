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
import android.widget.Toast;

import static android.content.Context.ACTIVITY_SERVICE;

public class MainActivity extends AppCompatActivity {
    IStudyBinder mStudyBinder;
    private TextView mResultTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultTv = (TextView)findViewById(R.id.result_tv);
        Intent intent = new Intent(this,NoAidlService.class);
        Intent intent0 = new Intent(this,StudyBinderService.class);
        bindService(intent, mNoAidlConnection, Context.BIND_AUTO_CREATE);
        bindService(intent0, mServiceConn, Context.BIND_AUTO_CREATE);
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getDeviceConfigurationInfo();

    }

    private ServiceConnection mServiceConn = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mStudyBinder = IStudyBinder.Stub.asInterface(service);
            try {

                mResultTv.setText(mResultTv.getText()+"\n"+mStudyBinder.studyBinder("SilenceDut"));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private ServiceConnection mNoAidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceInvoked(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void serviceInvoked(IBinder service) {

        if (service == null) {
            Toast.makeText(this, "未连接服务端或服务端被异常杀死", Toast.LENGTH_SHORT).show();
        } else {
            android.os.Parcel _data = android.os.Parcel.obtain();
            android.os.Parcel _reply = android.os.Parcel.obtain();
            String _result;
            try {
                _data.writeInterfaceToken("NoAidlService");
                _data.writeString("SilenceDut");
                service.transact(NoAidlService.TRANSACTION_studyBinder, _data, _reply, 0);
                _reply.readException();
                _result = _reply.readString();
                mResultTv.setText(mResultTv.getText()+"\n"+_result);
                Toast.makeText(this, _result + "", Toast.LENGTH_SHORT).show();

            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                _reply.recycle();
                _data.recycle();
            }
        }

    }
}
