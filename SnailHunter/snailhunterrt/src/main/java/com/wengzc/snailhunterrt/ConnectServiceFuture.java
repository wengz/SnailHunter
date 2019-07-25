package com.wengzc.snailhunterrt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ConnectServiceFuture implements Future<ISnailHunterService>, ServiceConnection {

    private boolean mResultReceived = false;
    private ISnailHunterService mResult;
    private boolean enterWaiting;

    @Override
    public ISnailHunterService get() throws ExecutionException, InterruptedException {
        try{
            return doGet(null);
        }catch (TimeoutException e){
            throw new AssertionError(e);
        }
    }

    @Override
    public ISnailHunterService get(long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, timeUnit));
    }

    private synchronized ISnailHunterService doGet (Long timeoutMs) throws InterruptedException, TimeoutException {
        if (mResultReceived){
            return mResult;
        }

        enterWaiting = true;
        if (timeoutMs == null){
            wait(0);
        }else if (timeoutMs > 0){
            wait(timeoutMs);
        }
        enterWaiting = false;
        if (!mResultReceived){
            throw new TimeoutException();
        }

        return mResult;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mResultReceived = true;
        mResult = ISnailHunterService.Stub.asInterface(iBinder);
        notifyAll();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mResultReceived = true;
        mResult = null;
        if (enterWaiting){
            notifyAll();
        }
    }

    @Override
    public synchronized boolean cancel(boolean b) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return mResultReceived || isCancelled();
    }
}
