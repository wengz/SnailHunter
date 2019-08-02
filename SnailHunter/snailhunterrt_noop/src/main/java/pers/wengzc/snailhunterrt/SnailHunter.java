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

import pers.wengzc.hunterkit.ByteCodeBridge;

/**
 * @author wengzc
 */
public class SnailHunter {

    private static final String HUNTER_PROCESS_NAME = "snailhunter";

    public static boolean isHunterProcess(Context context) {
        String processName = ProcessUtils.myProcessName(context);
        return processName != null && processName.endsWith(":" + HUNTER_PROCESS_NAME);
    }

    public static void install(Context context) {


    }


}
