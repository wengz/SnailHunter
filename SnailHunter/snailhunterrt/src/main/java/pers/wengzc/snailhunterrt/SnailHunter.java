package pers.wengzc.snailhunterrt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pers.wengzc.hunterKit.WriggleInfo;
import pers.wengzc.hunterKit.WriggleInfoHandler;
import pers.wengzc.snailhunterrt.ISnailHunterService;
import pers.wengzc.snailhunterrt.SnailHunterService;

import pers.wengzc.hunterKit.ByteCodeBridge;

/**
 * @author wengzc
 */
public class SnailHunter {

    public static Context sContext;

    private static  ConnectServiceFuture sConnectServiceFuture;

    public static void install (Context context){
        sContext = context;
        connectServiceIfNot();

        ByteCodeBridge.sWriggleInfoHandler = new WriggleInfoHandler() {
            @Override
            public void handleWriggleInfo(final WriggleInfo wriggleInfo) {

                HunterReportHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("xxx", "尝试获取 ISnailHunterService");
                        ISnailHunterService snailHunterService = getServiceSyncMayNull();
                        Log.d("xxx", "获取到ISnailHunterService");
                        if (snailHunterService != null){
                            try{
                                Log.d("xxx", "远程服务调用 snailHunterService.catchNewSnail");
                                Snail rtSnail = new Snail(wriggleInfo.processId, wriggleInfo.threadId, wriggleInfo.threadName, wriggleInfo.packageName, wriggleInfo.className, wriggleInfo.methodName,
                                        wriggleInfo.isMainThread, wriggleInfo.timeConstraint, wriggleInfo.executeTime);
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
