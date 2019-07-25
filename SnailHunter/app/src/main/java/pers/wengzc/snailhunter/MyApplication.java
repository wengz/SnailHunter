package pers.wengzc.snailhunter;

import android.app.Application;

import com.wengzc.snailhunterrt.SnailHunter;

/**
 * @author wengzc
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SnailHunter.install(this);
    }
}
