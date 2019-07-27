package pers.wengzc;

import com.alibaba.fastjson.JSON;

import org.objectweb.asm.tree.MethodNode;

import pers.wengzc.hunterKit.Action;

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

        boolean justMainThread;

        long timeConstraint;

        boolean inherited;

        public Action action;

    }

    public MethodManipulateArg getMethodManipulateArg (){
        MethodManipulateArg arg = new MethodManipulateArg();
        arg.justMainThread = config.justMainThread;
        arg.timeConstraint = config.timeConstraint;
        return arg;
    }

}
