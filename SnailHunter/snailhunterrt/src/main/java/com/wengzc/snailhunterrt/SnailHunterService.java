package com.wengzc.snailhunterrt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SnailHunterService extends Service {

    private ISnailHunterService.Stub mSnailHunterService = new ISnailHunterService.Stub() {
        @Override
        public void catchNewSnail(Snail snail) throws RemoteException {
            Log.d("xxx", "catchNewSnail: ");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mSnailHunterService;
    }
}
