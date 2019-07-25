package com.wengzc.snailhunterrt;

import android.content.Context;
import android.util.Log;

import pers.wengzc.hunterKit.ByteCodeBridge;
import pers.wengzc.hunterKit.Snail;
import pers.wengzc.hunterKit.SnailWatcher;

/**
 * @author wengzc
 */
public class SnailHunter {




    public static void install (Context context){
        ByteCodeBridge.sSnailWatcher = new SnailWatcher() {
            @Override
            public void onCatchSnail(Snail snail) {
                Log.d("xxx", "in snail hunter rt:"+snail);
            }
        };
    }

    private static  ConnectServiceFuture sConnectServiceFuture;

    private static void connectServiceIfNot (){
        if (sConnectServiceFuture == null){
            sConnectServiceFuture = new ConnectServiceFuture();

        }
    }
}
