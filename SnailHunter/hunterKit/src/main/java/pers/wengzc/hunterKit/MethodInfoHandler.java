package pers.wengzc.hunterkit;

/**
 * @author wengzc
 */
public interface MethodInfoHandler {

    /**
     * 超时函数运行时信息处理
     *
     * @param processId 进程ID
     * @param threadId 线程ID
     * @param threadName 线程名
     * @param packageName 包名
     * @param className 类名
     * @param methodName 方法名
     * @param startTime 开始时间(ns)
     * @param finishTime 结束时间(ns)
     * @param isMainThread 是否在主线程运行
     * @param mainThreadConstraint 是否在主线程约束
     * @param timeConstraint 时间约束(ms)
     */
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
