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

    public static void install (Context context){
        sContext = context;
        connectServiceIfNot();

        ByteCodeBridge.sWriggleInfoHandler = new WriggleInfoHandler() {
            @Override
            public void handleWriggleInfo(WriggleInfo wriggleInfo) {
                Log.d("xxx", "SnailHunter#onCatchSnail>>>"+ProcessThread.info());

                ISnailHunterService snailHunterService = getServiceSyncMayNull();
                if (snailHunterService != null){
                    Log.d("xxx", "SnailHunter#onCatchSnail>>> snailHunterService != null, invoke Service handle");
                    try{
                        Snail rtSnail = new Snail(wriggleInfo.packageName, wriggleInfo.className, wriggleInfo.methodName,
                                wriggleInfo.isMainThread, wriggleInfo.timeConstraint, wriggleInfo.executeTime);
                        snailHunterService.catchNewSnail(rtSnail);
                    }catch (Exception e){
                        Log.d("xxx", "SnailHunter#onCatchSnail>>> snailHunterService.catchNewSnail exception");
                    }
                }
            }
        };

    }

    private static  ConnectServiceFuture sConnectServiceFuture;

    private static void connectServiceIfNot (){
        if (sConnectServiceFuture == null){
            sConnectServiceFuture = new ConnectServiceFuture();
            if (sContext != null){
                Log.d("xxx", "connectServiceIfNot: ");
                Intent intent = new Intent(sContext, SnailHunterService.class);
                sContext.bindService(intent, sConnectServiceFuture, Context.BIND_AUTO_CREATE);
            }
        }
    }

    private static ISnailHunterService getServiceSyncMayNull (){
        if (sConnectServiceFuture == null){
            Log.d("xxx", "getServiceSyncMayNull, sConnectServiceFuture == null");
            return null;
        }else{
            try{
                return sConnectServiceFuture.get();
            }catch (Exception e){
                Log.d("xxx", "getServiceSyncMayNull, connect service failed.", e);
            }
            return null;
        }
    }

}
