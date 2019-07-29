package pers.wengzc.snailhunterrt;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import pers.wengzc.snailhunterrt.ISnailHunterService;

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
        Log.d("xxx", "synchronized ConnectServiceFuture doGet: ");
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
    public synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("xxx", "onServiceConnected: ");
        mResultReceived = true;
        mResult = ISnailHunterService.Stub.asInterface(iBinder);
        notifyAll();
    }

    @Override
    public synchronized void onServiceDisconnected(ComponentName componentName) {
        Log.d("xxx", "onServiceDisconnected: ");
        mResultReceived = true;
        mResult = null;
        notifyAll();
    }

    @Override
    public void onNullBinding(ComponentName name) {
        Log.d("xxx", "onNullBinding: ");
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
    public synchronized boolean isDone() {
        return mResultReceived || isCancelled();
    }
}
