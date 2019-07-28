package pers.wengzc.hunterkit;

/**
 * @author wengzc
 */
public class WriggleInfo {

    public WriggleInfo (String packageName, String className, String methodName, boolean isMainThread, long timeConstraint, long executeTime){
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.isMainThread = isMainThread;
        this.timeConstraint = timeConstraint;
        this.executeTime = executeTime;
    }

    @Override
    public String toString() {
        return "processId="+processId+
                " threadId="+threadId+
                " threadName="+threadName+
                " packageName="+packageName+
                " className="+className+
                " methodName="+methodName+
                " isMainThread="+isMainThread+
                " timeConstraint="+timeConstraint+
                " executeTime="+executeTime;
    }

    public int processId;

    public long threadId;

    public String threadName;

    public String packageName;

    public String className;

    public String methodName;

    public boolean isMainThread;

    public long timeConstraint;

    public long executeTime;
}
