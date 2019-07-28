package pers.wengzc.snailhunter;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ScriptConfigVal {

    public String[] include;

    public String[] exclude;

    public List<ConfigItem> includeConfig;
    public List<ConfigItem> excludeConfig;

    /**
     * 返回排除项中第一个匹配，若无则返回null
     *
     * @return
     */
    public ConfigItem matchExclude (String packageName, String className, String methodName){
        className = toSimpleClassName(className);
        if (excludeConfig != null){
            for (ConfigItem item : excludeConfig){
                if (item.match(packageName, className, methodName)){
                    return item;
                }
            }
        }
        return null;
    }

    private static String toSimpleClassName (String className){
        if (className.contains(".")){
            return className.substring(className.lastIndexOf(".")+1);
        }
        return className;
    }

    /**
     * 返回包含项中第一个匹配，若无则返回null
     *
     * @return
     */
    public ConfigItem matchInclude (String packageName, String className, String methodName){
        className = toSimpleClassName(className);

        if (includeConfig != null){
            for (ConfigItem item : includeConfig){
                if (item.match(packageName, className, methodName)){
                    return item;
                }
            }
        }
        return null;
    }


    public void adjuestInternal  (){
        if (include != null && include.length > 0){
            includeConfig = new ArrayList<>();
            for (String includeStr : include){
                ConfigItem configItem = JSON.parseObject(includeStr, ConfigItem.class);
                if (!includeStr.contains("timeConstraint")){
                    configItem.timeConstraint = -1;
                }
                includeConfig.add(configItem);
            }
        }

        if (exclude != null && exclude.length > 0){
            excludeConfig = new ArrayList<>();
            for (String exclueStr : exclude){
                ConfigItem configItem = JSON.parseObject(exclueStr, ConfigItem.class);
                excludeConfig.add(configItem);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----ScriptConfig----\n");
        sb.append("----includeConfig\n");
        if (includeConfig != null){
            for (ConfigItem it : includeConfig){
                sb.append(it.toString()+"\n");
            }
        }
        sb.append("----excludeConfig\n");
        if (excludeConfig != null){
            for (ConfigItem it : excludeConfig){
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



        public boolean match (String packageName, String className, String methodName){
            packageName = packageName == null ? "" : packageName;
            className = className == null ? "" : className;
            methodName = methodName == null ? "" : methodName;

            if (packageConstraint != null && packageConstraint.length() > 0 && !Pattern.compile(packageConstraint).matcher(packageName).matches()){
                return false;
            }
            if (classConstraint != null && classConstraint.length() > 0 && !Pattern.compile(classConstraint).matcher(className).matches()){
                return false;
            }
            if (methodConstraint != null && methodConstraint.length() > 0 && !Pattern.compile(methodConstraint).matcher(methodName).matches()){
                return false;
            }
            return true;
        }

        public String packageConstraint;

        //仅类名，不包含包前缀
        public String classConstraint;

        public String methodConstraint;

        public boolean justMainThread;

        public long timeConstraint;

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


        public MethodManipulateArg getMethodManipulateArg (){
            MethodManipulateArg arg = new MethodManipulateArg();
            arg.justMainThread = justMainThread;
            arg.timeConstraint = timeConstraint;
            return arg;
        }

    }

}
