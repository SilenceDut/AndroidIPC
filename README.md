#AndroidIPC
[理解Binder框架](http://www.jianshu.com/p/9aa83e8b9ddb)
利用Binder不使用AIDL实现IPC的过程

    服务端:
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
                                         int flags) throws RemoteException
            {
                switch (code)
                {
                    case TRANSACTION_studyBinder:
                    {
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
    
    注册服务,并指定为远程进程
    <service
        android:name=".NoAidlService"
        android:process=":remote"
        />
        
    客户端:
    private ServiceConnection mNoAidlConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder clientBinder) {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                String _result;
                try
                {
                    _data.writeInterfaceToken("NoAidlService");
                    _data.writeString("SilenceDut");
                    service.transact(NoAidlService.TRANSACTION_studyBinder, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                    mResultTv.setText(mResultTv.getText()+"\n"+_result);
                    Toast.makeText(this, _result + "", Toast.LENGTH_SHORT).show();
    
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                } finally
                {
                    _reply.recycle();
                    _data.recycle();
                }
            }
    
            @Override
            public void onServiceDisconnected(ComponentName name) {
    
            }
        };
        
         Intent intent = new Intent(this,NoAidlService.class);
         bindService(intent, mNoAidlConnection, Context.BIND_AUTO_CREATE);
    
具体的过程是NoAidlService需要先在Manifest注册信息,这相当于是将服务信息注册到ServiceManage。客户端用
Intent将NoAidlService的服务名等信息包装,通过bindService在ServiceManage里寻找NoAidlService,如果
成功,则通过onServiceConnected回调得到服务端的信息ComponentName,和服务通信接口clientBinder。这个clientBinder和
NoAidlService通过onBind返回的mNoAidlBinder相似,如果是远程服务,就不是同一个对象,可以理解为是通过Binder驱动得到的一个代理ProxyBinder对象,但看起来和mNoAidlBinder是同一个对象,其实不是的。
但如果在一个进程,两个就是同一个对象。
然后clientBinder通过transact指定服务端对应的函数code比如TRANSACTION_studyBinder,来调用服务端的想要函数,通过Binder驱动,
最后会调用到服务端NoAidlBinder的onTransact,通过判断code来确定相应的函数,然后Binder再将结果返回,这个过程对Client是阻塞性的,
所以客户端调用最好是在异步线程和服务端通信。对服务端,系统会为每个服务提供线程池,这也容易想到,因为不可能让不同的服务为在调用服务时相互等待。

使用AIDL来和Service通信
AIDL，即Android Interface Definition Language，Android接口定义语言。它是一种IDL语言，可以拿来生成用于IPC的代码。[AIDL实现IPC通信Demo](https://github.com/SilenceDut/AndroidIPC/blob/master/app/src/main/java/com/silencedut/androidipc/StudyBinderService.java),
其实就是AIDL文件生成一个帮助类,屏蔽parcel的读写细节,让客户端使用者专注于业务的实现。有一点需要注意的是,如果Service不是在一个新的进程,通过IBinder.queryLocalInterface(DESCRIPTOR)的方式得到本地的Binder对象,就不会涉及到跨进程通信和Binder驱动的调用。这点和不使用AIDL实现IPC是一样的。