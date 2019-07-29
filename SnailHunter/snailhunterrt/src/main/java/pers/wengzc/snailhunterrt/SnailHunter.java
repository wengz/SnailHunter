package pers.wengzc.snailhunterrt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

    private static  ConnectServiceFuture sConnectServiceFuture;

    public static boolean isHunterProcess (Context context){
        String processName = ProcessUtils.myProcessName(context);
        return processName != null && processName.endsWith(":" + HUNTER_PROCESS_NAME);
    }

    public static void install (Context context){
        if (isHunterProcess(context)){
            return;
        }

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
                HunterReportHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {

                        //只检查主线程，非主线程运行状态不捕捉
//                        if (mainThreadConstraint && !isMainThread ){
//                            return;
//                        }
//
//                        //时间约束
//                        long costTime = finishTime - startTime;
//                        if (costTime < timeConstraint * 1000000){
//                            return;
//                        }

                        //Log.d("xxx", "尝试获取 ISnailHunterService");
                        Log.d("xxx", "run: className="+className+" methodName="+methodName);
                        ISnailHunterService snailHunterService = getServiceSyncMayNull();
                        //Log.d("xxx", "获取到ISnailHunterService");
                        if (snailHunterService != null){
                            try{
                                //Log.d("xxx", "远程服务调用 snailHunterService.catchNewSnail");
                                Snail rtSnail = new Snail( processId,  threadId,  threadName,
                                         packageName,
                                         className,
                                         methodName,
                                         startTime,
                                         finishTime,
                                         isMainThread,
                                         mainThreadConstraint,
                                         timeConstraint);
                                snailHunterService.catchNewSnail(rtSnail);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

        };

    }

    private static void connectServiceIfNot (){
        if (sConnectServiceFuture == null){
            sConnectServiceFuture = new ConnectServiceFuture();
            if (sContext != null){
                Log.d("xxx", "开始 bindService");
                Intent intent = new Intent(sContext, SnailHunterService.class);
                sContext.bindService(intent, sConnectServiceFuture, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private static ISnailHunterService getServiceSyncMayNull (){
        if (sConnectServiceFuture == null){
            return null;
        }else{
            try{
                return sConnectServiceFuture.get();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

}
