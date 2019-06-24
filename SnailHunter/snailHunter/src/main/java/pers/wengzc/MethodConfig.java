package pers.wengzc;

import org.objectweb.asm.tree.MethodNode;

import pers.wengzc.hunterKit.Action;

public class MethodConfig {

    public MethodIdentify methodIdentify;

    public Config config;

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

}
