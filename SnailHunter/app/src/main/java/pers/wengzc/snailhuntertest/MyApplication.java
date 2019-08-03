package pers.wengzc.snailhuntertest;

import android.app.Application;
import android.util.Log;

import pers.wengzc.hunterkit.HunterTarget;
import pers.wengzc.snailhunterrt.SnailHunter;

/**
 * @author wengzc
 */
public class MyApplication extends Application {

    @HunterTarget
    @Override
    public void onCreate() {
        //long start = System.nanoTime();
        super.onCreate();

        SnailHunter.install(this);

        //long cost = System.nanoTime() - start;
        //Log.d("xxx", "onCreate: cost ="+(cost/1000000f));
    }
}
