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

            //时间约束已经在注入的字节码中判定
//            long costTime = finishTime - startTime;
//            if (costTime < timeConstraint * 1000000) {
//                return;
//            }

            //线程判定在此处处理,注入的字节码中不应该做过多的操作
            boolean isMainThread = AndroidUtil.isInUIThread();;
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
