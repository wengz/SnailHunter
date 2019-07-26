package pers.wengzc.snailhunterrt;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import pers.wengzc.snailhunterrt.ISnailHunterService;
import pers.wengzc.snailhunterrt.ISnailWatcher;
import pers.wengzc.snailhunterrt.ui.Notifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class SnailHunterService extends Service {

    private List<Snail> mSnails = new LinkedList<>();

    private final RemoteCallbackList<ISnailWatcher> mSnailWatcher = new RemoteCallbackList<>();

    private ISnailHunterService.Stub mSnailHunterService = new ISnailHunterService.Stub() {
        @Override
        public void catchNewSnail(Snail snail) throws RemoteException {
            mSnails.add(snail);
            if (snail.timeConstraint >= 0){
                //时间约束大于等于0的进行通知栏提醒
                Notifier.notifyNewSnail(getApplicationContext());
            }else{
                //时间约束小于0的进行logcat记录
                Log.d("xxx", ">>>SnailHunterService#catchNewSnail>>>"+snail);
            }
        }

        @Override
        public List<Snail> getAllSnail() throws RemoteException {
            return mSnails;
        }

        @Override
        public void registerNewSnailWatcher(ISnailWatcher newWatcher) throws RemoteException {
            mSnailWatcher.register(newWatcher);
        }

        @Override
        public void unregisterNewSnailWatcher(ISnailWatcher watcher) throws RemoteException {
            mSnailWatcher.unregister(watcher);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mSnailHunterService;
    }
}
