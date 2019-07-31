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
import java.util.concurrent.CopyOnWriteArrayList;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

public class SnailHunterService extends Service {

    private Object dataLock = new Object();

    private static final int DATA_CAPACCITY_SIZE = 1000;

    private List<Snail> mSnails = new LinkedList<>();


    private final RemoteCallbackList<ISnailWatcher> mSnailWatcher = new RemoteCallbackList<>();

    private void addNewSnail (Snail snail){
        snail.leafInvoke = true;
        synchronized (dataLock){
            int size = mSnails.size();
            if (size > DATA_CAPACCITY_SIZE){
               mSnails.subList(size/2, size).clear();
            }

            for (Snail sn : mSnails){
                if (sn.leafInvoke){
                    if (snail.wrap(sn)){
                        snail.leafInvoke = false;
                        sn.leafInvoke = true;
                    }
                }
            }
            mSnails.add(0, snail);
        }
    }

    private ISnailHunterService.Stub mSnailHunterService = new ISnailHunterService.Stub() {
        @Override
        public void catchNewSnail(Snail snail)  {
            Log.d(Constant.TAG, "服务端函数信息处理");

            //只检查主线程，非主线程运行状态不捕捉
            if (snail.mainThreadConstraint && !snail.isMainThread ){
                return;
            }

            //时间约束
            long costTime = snail.finishTime - snail.startTime;
            if (costTime < snail.timeConstraint * 1000000){
                return;
            }

            snail.executeTime = costTime;
            addNewSnail(snail);
            Log.d(Constant.TAG, ">>>函数运行时报告>>>"+snail);
            if (snail.timeConstraint >= 0){
                //时间约束大于等于0的进行通知栏提醒
                Notifier.notifyNewSnail(getApplicationContext());
            }
            notifySnailDataChanged(snail);
        }

        private void notifySnailDataChanged (Snail snail){
            try{
                int count = mSnailWatcher.beginBroadcast();
                for (int i = 0; i < count; i++){
                    ISnailWatcher sw = mSnailWatcher.getBroadcastItem(i);
                    sw.onCatchNewSnail(snail);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mSnailWatcher.finishBroadcast();
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

        @Override
        public void clear() throws RemoteException {
            synchronized (dataLock){
                mSnails.clear();
            }
            notifySnailDataChanged(null);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return mSnailHunterService;
    }
}
