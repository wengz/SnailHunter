package pers.wengzc.hunterKit;

import android.util.Log;

/**
 * @author wengzc
 */
public class SnailHunter {

    public static final String TAG = "SnailHunter";

    public static void handle(Snail snail){
        Log.d(TAG, "execute time="+snail.executeTime);
    }

}
