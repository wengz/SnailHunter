package pers.wengzc.hunterKit;

import android.os.Looper;

public class AndroidUtil {

    public static boolean isInUIThread (){
        return Looper.myLooper() == Looper.getMainLooper();
    }
}