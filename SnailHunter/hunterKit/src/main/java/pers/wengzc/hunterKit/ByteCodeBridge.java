package pers.wengzc.hunterkit;

import android.os.Process;

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
            int processId = Process.myPid();
            Thread curThread = Thread.currentThread();
            long threadId = curThread.getId();
            String threadName = curThread.getName();
            boolean isMainThread = AndroidUtil.isInUIThread();

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
