package pers.wengzc;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class ScriptConfigVal {

    public String[] include;

    public String[] exclude;

    public List<ConfigItem> includeConfig;
    public List<ConfigItem> excludeConfig;

    public void adjuestInternal  (){
        if (include != null && include.length > 0){
            includeConfig = new ArrayList<>();
            for (String includeStr : include){
                ConfigItem configItem = JSON.parseObject(includeStr, ConfigItem.class);
                includeConfig.add(configItem);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----ScriptConfigVal----\n");
        sb.append("----includeConfig\n");
        if (includeConfig != null){
            for (ConfigItem it : includeConfig){
                sb.append(it.toString()+"\n");
            }
        }
        return sb.toString();
    }

    public static class ConfigItem {

        @Override
        public String toString() {
            return "packageConstraint="+packageConstraint+
                    " classConstraint="+classConstraint+
                    " methodConstraint="+methodConstraint+
                    " timeConstraint="+timeConstraint+
                    " justMainThread="+ justMainThread;
        }

        public String packageConstraint;

        public String classConstraint;

        public String methodConstraint;

        public long timeConstraint;

        public boolean justMainThread;

        public String getPackageConstraint() {
            return packageConstraint;
        }

        public void setPackageConstraint(String packageConstraint) {
            this.packageConstraint = packageConstraint;
        }

        public String getClassConstraint() {
            return classConstraint;
        }

        public void setClassConstraint(String classConstraint) {
            this.classConstraint = classConstraint;
        }

        public String getMethodConstraint() {
            return methodConstraint;
        }

        public void setMethodConstraint(String methodConstraint) {
            this.methodConstraint = methodConstraint;
        }

        public long getTimeConstraint() {
            return timeConstraint;
        }

        public void setTimeConstraint(long timeConstraint) {
            this.timeConstraint = timeConstraint;
        }

        public boolean isJustMainThread() {
            return justMainThread;
        }

        public void setJustMainThread(boolean justMainThread) {
            this.justMainThread = justMainThread;
        }

    }

}
