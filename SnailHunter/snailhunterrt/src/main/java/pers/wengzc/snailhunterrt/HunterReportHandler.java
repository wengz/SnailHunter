package pers.wengzc.snailhunterrt;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author wengzc
 */
public class HunterReportHandler extends Handler {

    private static volatile HunterReportHandler sInstance;

    private static volatile Object sMainThreadLock = new Object();

    private static volatile HunterReportHandler sMainInstance;

    private static HandlerThread sHandlerThread = new HandlerThread("SnailHunterReportThread");

    private static HandlerThread sMainHandlerThread = new HandlerThread("MainSnailHunterReportThread");

    private HunterReportHandler (Looper looper){
        super(looper);
    }

    static HunterReportHandler getMainInstance() {
        if (sMainInstance == null){
            synchronized (sMainThreadLock){
                if (sMainInstance == null){
                    sMainHandlerThread.start();
                    sMainInstance = new HunterReportHandler(sMainHandlerThread.getLooper());
                }
            }
        }
        return sMainInstance;
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
