package pers.wengzc.hunterkit;

import android.os.Process;
import android.util.Log;

/**
 * @author wengzc
 */
public class ByteCodeBridge {

    public static final String TAG = "SnailHunter";

    public static MethodInfoHandler sMethodInfoHandler;

    public static void handle(
                              String packageName,
                              String className,
                              String methodName,
                              long startTime,
                              long finishTime,
                              boolean mainThreadConstraint,
                              long timeConstraint
                              ){
        if (sMethodInfoHandler != null){

            //时间约束
//            long costTime = finishTime - startTime;
//            if (costTime < timeConstraint * 1000000) {
//                return;
//            }

            Log.d(TAG, "handle: startTime="+(startTime/1000000f)+" finishTime="+(finishTime/1000000f));

            boolean isMainThread = AndroidUtil.isInUIThread();
            //只检查主线程，非主线程运行状态不捕捉
            if (mainThreadConstraint && !isMainThread) {
                return;
            }

            int processId = Process.myPid();
            Thread curThread = Thread.currentThread();
            long threadId = curThread.getId();
            String threadName = curThread.getName();

            sMethodInfoHandler.handleMethodRuntimeInfo( processId,  threadId,  threadName,
                     packageName,
                     className,
                     methodName,
                     startTime,
                     finishTime,
                     isMainThread,
                     mainThreadConstraint,
                     timeConstraint);
        }
    }

}
