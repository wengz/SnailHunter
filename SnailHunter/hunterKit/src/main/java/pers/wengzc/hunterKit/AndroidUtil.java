package pers.wengzc.hunterkit;

import android.os.Looper;

public class AndroidUtil {

    public static boolean isInUIThread (){
        return Looper.myLooper() == Looper.getMainLooper();
    }
}