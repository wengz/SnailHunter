package pers.wengzc.hunterKit;

import android.os.Process;
import android.util.Log;

/**
 * @author wengzc
 */
public class ByteCodeBridge {

    public static final String TAG = "SnailHunter";

    public static WriggleInfoHandler sWriggleInfoHandler;

    public static void handle(WriggleInfo wriggleInfo){
        if (sWriggleInfoHandler != null){
            int processId = Process.myPid();
            Thread curThread = Thread.currentThread();
            long threadId = curThread.getId();
            String threadName = curThread.getName();
            wriggleInfo.processId = processId;
            wriggleInfo.threadId = threadId;
            wriggleInfo.threadName = threadName;
            sWriggleInfoHandler.handleWriggleInfo(wriggleInfo);
        }
    }

}
