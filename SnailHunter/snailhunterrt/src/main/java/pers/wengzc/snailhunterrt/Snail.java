package pers.wengzc.snailhunterrt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wengzc
 */
public class Snail implements Parcelable {

    public Snail(String packageName, String className, String methodName, boolean isMainThread, long timeConstraint, long executeTime){
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.isMainThread = isMainThread;
        this.timeConstraint = timeConstraint;
        this.executeTime = executeTime;
    }

    protected Snail(Parcel in) {
        packageName = in.readString();
        className = in.readString();
        methodName = in.readString();
        isMainThread = in.readByte() != 0;
        timeConstraint = in.readLong();
        executeTime = in.readLong();
    }

    @Override
    public String toString() {
        return "packageName="+packageName+" className="+className+" methodName="+methodName+
                " isMainThread="+isMainThread+" timeConstraint="+timeConstraint+" executeTime="+executeTime;
    }

    public String packageName;

    public String className;

    public String methodName;

    boolean isMainThread;

    long timeConstraint;

    long executeTime;

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Snail> CREATOR = new Creator<Snail>() {
        @Override
        public Snail createFromParcel(Parcel in) {
            return new Snail(in);
        }

        @Override
        public Snail[] newArray(int size) {
            return new Snail[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(packageName);
        dest.writeString(className);
        dest.writeString(methodName);
        dest.writeByte((byte) (isMainThread ? 1 : 0));
        dest.writeLong(timeConstraint);
        dest.writeLong(executeTime);
    }
}
