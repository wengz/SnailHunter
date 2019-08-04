package pers.wengzc.snailhunter;

import com.alibaba.fastjson.JSON;

import org.objectweb.asm.tree.MethodNode;

import pers.wengzc.hunterkit.Action;

public class MethodConfig {

    public MethodIdentify methodIdentify;

    public Config config;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public boolean match (MethodNode methodNode){
        if (methodNode.name.equals(methodIdentify.methodName)
        && methodNode.desc.equals(methodIdentify.methodDescription)){
            return true;
        }
        return false;
    }


    public static class MethodIdentify {

        public String methodName;

        public String methodDescription;

    }

    public static class Config {

        /**
         * 是否只检测主线程
         */
        boolean justMainThread;

        /**
         * 时间限制
         */
        long timeConstraint;

        /**
         * 是否可继承
         */
        boolean inherited;

        /**
         * 配置动作
         */
        public Action action;

    }

    public MethodManipulateArg getMethodManipulateArg (){
        MethodManipulateArg arg = new MethodManipulateArg();
        arg.justMainThread = config.justMainThread;
        arg.timeConstraint = config.timeConstraint;
        return arg;
    }

}
