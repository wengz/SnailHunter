package pers.wengzc.hunterKit;

/**
 * @author wengzc
 */
public class Snail {

    public Snail (String packageName, String className, String methodName, boolean isMainThread, long timeConstraint, long executeTime){
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.isMainThread = isMainThread;
        this.timeConstraint = timeConstraint;
        this.executeTime = executeTime;
    }

    public String packageName;

    public String className;

    public String methodName;

    boolean isMainThread;

    long timeConstraint;

    long executeTime;

}
