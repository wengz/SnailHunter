package pers.wengzc.snailhunterrt;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.chrono.ThaiBuddhistChronology;

/**
 * @author wengzc
 */
public class Snail  implements Parcelable{

    public int processId;
    public long threadId;
    public String threadName;
    public String packageName;
    public String className;
    public String methodName;
    //函数开始时间（纳秒）
    public long startTime;
    //函数结束时间（纳秒）
    public long finishTime;
    public boolean isMainThread;
    public boolean mainThreadConstraint;
    //运行时间约束(毫秒)
    public long timeConstraint;
    //实际运行时间(纳秒)
    public long executeTime;
    //栈尾调用
    public boolean leafInvoke;

    public boolean wrap (Snail other){
        if (this.processId == other.processId && this.threadId == other.threadId && this.startTime <= other.startTime && this.finishTime >= other.finishTime){
            return true;
        }
        return false;
    }

    public Snail(int processId, long threadId, String threadName,
                 String packageName,
                 String className,
                 String methodName,
                 long startTime,
                 long finishTime,
                 boolean isMainThread,
                 boolean mainThreadConstraint,
                 long timeConstraint){
        this.processId = processId;
        this.threadId = threadId;
        this.threadName = threadName;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.isMainThread = isMainThread;
        this.mainThreadConstraint = mainThreadConstraint;
        this.timeConstraint = timeConstraint;
    }

    protected Snail(Parcel in) {
        processId = in.readInt();
        threadId = in.readLong();
        threadName = in.readString();
        packageName = in.readString();
        className = in.readString();
        methodName = in.readString();
        startTime = in.readLong();
        finishTime = in.readLong();
        isMainThread = in.readByte() != 0;
        mainThreadConstraint = in.readByte() != 0;
        timeConstraint = in.readLong();
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
    public String toString() {
        return "processId="+processId +
                " threadId="+threadId+
                " threadName="+threadName+
                " packageName="+packageName+
                " className="+className+
                " methodName="+methodName+
                " startTime(ms)="+(startTime/1000000)+
                " finishTime(ms)="+(finishTime/1000000)+
                " isMainThread="+isMainThread+
                " mainThreadConstraint="+mainThreadConstraint+
                " timeConstraint(ms)="+timeConstraint+
                " executeTime=(ms)"+(executeTime/1000000f);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(processId);
        dest.writeLong(threadId);
        dest.writeString(threadName);
        dest.writeString(packageName);
        dest.writeString(className);
        dest.writeString(methodName);
        dest.writeLong(startTime);
        dest.writeLong(finishTime);
        dest.writeByte((byte) (isMainThread ? 1 : 0));
        dest.writeByte((byte) (mainThreadConstraint ? 1 : 0));
        dest.writeLong(timeConstraint);
    }
}
