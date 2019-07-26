package pers.wengzc.snailhunterrt;

import android.os.Process;

/**
 * @author wengzc
 */
public class ProcessThread {

    public static String info (){
        int processId = Process.myPid();
        long threadId = Thread.currentThread().getId();
        return "processId="+processId+" threadId="+threadId;
    }
}
