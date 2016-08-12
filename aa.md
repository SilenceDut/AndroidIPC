##Android 应用点击创建启动过程

应用安装的时候,通过PMS解析apk的AndroidManifest.xml文件,提取出这个apk的信息写入到packages.xml文件中，这些信息包括：权限、应用包名、icon、APK的安装位置、版本、userID等等。packages.xml文件位于系统目录下/data/system/packages.xml。

系统的会在启动时也可以认为开机时启动常用的服务,如ActivityManagerService(AMS),PackageManagerService(PMS),WindowManagerService(WMS),以及ServiceManager(SM),用于管理各种服务,详细的管理方式见[理解Binder框架](http://www.jianshu.com/p/9aa83e8b9ddb)。
同时桌面Launcher会为安装过的应用生成不同的应用入口,Launcher本身也是一个Activity。

下面分析点击应用图标的到应用启动的过程,拿[NBAPlus](https://github.com/SilenceDut/NBAPlus)这个应用来举例。这里主要是应用端的过程,服务端也就是AMS少量涉及,同时以大体框架为主,不深入代码细节。

- ActivityThread:App的真正入口,通过调用main()App开始真正运行，同时开启消息循环队列，虽然不是一个真正的线程,但一般所在的线程被称为UI线程或主线程。
- ApplicationThread :应用需要和远程服务AMS等通信,而Binder只能单项通信,而AMS等服务想控制应用需要应用程序提供一个Binder接口,ApplicationThread就是这个Binder接口,用于通过远程服务调用本地的方法。
- ActivityManagerProxy :AMS远程服务在本地的代理。
- ApplicationThreadProxy :ApplicationThread在远程服务AMS的代理。
