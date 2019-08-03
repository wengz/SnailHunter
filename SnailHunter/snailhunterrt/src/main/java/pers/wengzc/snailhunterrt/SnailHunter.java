package pers.wengzc.snailhunterrt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pers.wengzc.hunterkit.MethodInfoHandler;
import pers.wengzc.snailhunterrt.ISnailHunterService;
import pers.wengzc.snailhunterrt.SnailHunterService;

import pers.wengzc.hunterkit.ByteCodeBridge;

/**
 * @author wengzc
 */
public class SnailHunter {

    private static final String HUNTER_PROCESS_NAME = "snailhunter";

    public static Context sContext;

    private static ConnectServiceFuture sConnectServiceFuture;

    private static ExecutorService executorService;

    private static final int TASK_QUEUE_CAPACITY = 3000;

    public static boolean isHunterProcess(Context context) {
        String processName = ProcessUtils.myProcessName(context);
        return processName != null && processName.endsWith(":" + HUNTER_PROCESS_NAME);
    }

    public static void install(Context context) {
        if (isHunterProcess(context)) {
            return;
        }

        final LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue<Runnable>(TASK_QUEUE_CAPACITY);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().build();
        executorService = new ThreadPoolExecutor(3, 5,
                1000L, TimeUnit.MILLISECONDS,
                blockingQueue,
                threadFactory, new ThreadPoolExecutor.AbortPolicy());

        sContext = context;
        connectServiceIfNot();

        ByteCodeBridge.sMethodInfoHandler = new MethodInfoHandler() {

            @Override
            public void handleMethodRuntimeInfo(final int processId, final long threadId, final String threadName,
                                                final String packageName,
                                                final String className,
                                                final String methodName,
                                                final long startTime,
                                                final long finishTime,
                                                final boolean isMainThread,
                                                final boolean mainThreadConstraint,
                                                final long timeConstraint) {
                //只检查主线程，非主线程运行状态不捕捉
                if (mainThreadConstraint && !isMainThread) {
                    return;
                }

                //时间约束
                long costTime = finishTime - startTime;
                if (costTime < timeConstraint * 1000000) {
                    return;
                }

                try{
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {

                            ISnailHunterService snailHunterService = getServiceSyncMayNull();
                            if (snailHunterService != null) {
                                try {
                                    Snail rtSnail = new Snail(processId, threadId, threadName,
                                            packageName,
                                            className,
                                            methodName,
                                            startTime,
                                            finishTime,
                                            isMainThread,
                                            mainThreadConstraint,
                                            timeConstraint);
                                    //Log.d(Constant.TAG, "开始远程服务调用，报告函数信息");
                                    snailHunterService.catchNewSnail(rtSnail);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }catch (RejectedExecutionException e){
                    Log.d(Constant.TAG, "函数信息提交线程任务异常", e);
                    int remainingCapacity = blockingQueue.remainingCapacity();
                    if (remainingCapacity <= 1){
                        Log.d(Constant.TAG, "待处理函数信息过多，清除历史消息");
                        //待处理任务过多，进行清除
                        blockingQueue.clear();
                    }
                }

            }

        };

    }

    private static void connectServiceIfNot() {
        if (sConnectServiceFuture == null) {
            sConnectServiceFuture = new ConnectServiceFuture();
            if (sContext != null) {
                Intent intent = new Intent(sContext, SnailHunterService.class);
                sContext.bindService(intent, sConnectServiceFuture, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private static ISnailHunterService getServiceSyncMayNull() {
        if (sConnectServiceFuture == null) {
            return null;
        } else {
            try {
                return sConnectServiceFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
