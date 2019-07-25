package pers.wengzc.hunterKit;

import android.util.Log;

/**
 * @author wengzc
 */
public class ByteCodeBridge {

    public static final String TAG = "SnailHunter";

    public static SnailWatcher sSnailWatcher;

    public static void handle(Snail snail){
        Log.d(TAG, "snail="+snail);

        if (sSnailWatcher != null){
            sSnailWatcher.onCatchSnail(snail);
        }
    }

}
