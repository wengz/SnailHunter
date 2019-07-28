package pers.wengzc.hunterkit;

/**
 * @author wengzc
 */
public interface MethodInfoHandler {

    void handleMethodRuntimeInfo(int processId, long threadId, String threadName,
                                 String packageName,
                                 String className,
                                 String methodName,
                                 long startTime,
                                 long finishTime,
                                 boolean isMainThread,
                                 boolean mainThreadConstraint,
                                 long timeConstraint
    );

}
