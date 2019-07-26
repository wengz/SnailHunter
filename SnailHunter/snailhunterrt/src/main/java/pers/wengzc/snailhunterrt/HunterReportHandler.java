package pers.wengzc.snailhunterrt;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author wengzc
 */
public class HunterReportHandler extends Handler {

    private static volatile HunterReportHandler sInstance;

    private static HandlerThread sHandlerThread = new HandlerThread("SnailHunterReportThread");

    private HunterReportHandler (Looper looper){
        super(looper);
    }

    static HunterReportHandler getInstance() {
        if (sInstance == null){
            synchronized (HunterReportHandler.class){
                if (sInstance == null){
                    sHandlerThread.start();
                    sInstance = new HunterReportHandler(sHandlerThread.getLooper());
                }
            }
        }
        return sInstance;
    }

}
