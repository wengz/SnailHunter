package pers.wengzc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import pers.wengzc.hunterKit.HunterTarget;

public class AnnotationConfigHelper {

    public static ClassPool cp;

    public static Map<String, List<MethodConfig>[]> classMethodConfig;

    public static List<MethodConfig> getClassSelfMethodConfig (String className){
        if (!classMethodConfig.containsKey(className)){
            initClassMethodConfig(className);
        }
        return classMethodConfig.get(className)[0];
    }

    public static List<MethodConfig> getClassInheritMethodConfig (String className){
        if (!classMethodConfig.containsKey(className)){
            initClassMethodConfig(className);
        }
        return classMethodConfig.get(className)[1];
    }

    private static void initClassMethodConfig (String className){
        try{
            CtClass ctClass = cp.getCtClass(className);
            ArrayList<MethodConfig> selfConfig = new ArrayList<>();

            //方法注解
            CtMethod[] ctMethods = ctClass.getDeclaredMethods();
            if (ctMethods != null){
                for (CtMethod method : ctMethods){
                    HunterTarget ma = (HunterTarget) method.getAnnotation(HunterTarget.class);
                    if (ma != null){
                        String methodName = method.getName();
                        String description = method.getSignature();
                        MethodConfig.MethodIdentify methodIdentify = new MethodConfig.MethodIdentify();
                        methodIdentify.methodName = methodName;
                        methodIdentify.methodDescription = description;

                        MethodConfig.Config config = new MethodConfig.Config();
                        config.justMainThread = ma.justMainThread();
                        config.timeConstraint = ma.timeConstraint();
                        config.inherited = ma.inherited();
                        config.action = ma.action();

                        MethodConfig methodConfig = new MethodConfig();
                        methodConfig.methodIdentify = methodIdentify;
                        methodConfig.config = config;
                        selfConfig.add(methodConfig);
                    }
                }
            }

            //类上注解
            HunterTarget classAnnotation = (HunterTarget) ctClass.getAnnotation(HunterTarget.class);
            if (classAnnotation != null){
                for (CtMethod m : ctMethods){
                    String methodName = m.getName();
                    String description = m.getSignature();
                    MethodConfig.MethodIdentify methodIdentify = new MethodConfig.MethodIdentify();
                    methodIdentify.methodName = methodName;
                    methodIdentify.methodDescription = description;

                    MethodConfig.Config config = new MethodConfig.Config();
                    config.justMainThread = classAnnotation.justMainThread();
                    config.timeConstraint = classAnnotation.timeConstraint();
                    config.inherited = classAnnotation.inherited();
                    config.action = classAnnotation.action();

                    MethodConfig methodConfig = new MethodConfig();
                    methodConfig.methodIdentify = methodIdentify;
                    methodConfig.config = config;
                    selfConfig.add(methodConfig);
                }
            }

            //父类影响
            CtClass superClass = ctClass.getSuperclass();
            if (superClass != null){
                List<MethodConfig> inheritedConfig = getClassInheritMethodConfig(superClass.getName());
                selfConfig.addAll(inheritedConfig);
            }

            //接口影响
            CtClass[] interfaces = ctClass.getInterfaces();
            if (interfaces != null){
                for (CtClass itf : interfaces){
                    List<MethodConfig> itfMethodConfig = getClassInheritMethodConfig(itf.getName());
                    selfConfig.addAll(itfMethodConfig);
                }
            }

            List<MethodConfig> inheritConifg = filterInheritMethodConfig(selfConfig);

            List<MethodConfig>[] val = new ArrayList[2];
            val[0] = selfConfig;
            val[1] = inheritConifg;
            classMethodConfig.put(className, val);

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private static final String CONSTRUCT_METHOD_NAME = "<init>";

    public static List<MethodConfig> filterInheritMethodConfig (List<MethodConfig> mcs){
        List<MethodConfig> res = new ArrayList<>();
        if (mcs != null){
            for ( MethodConfig mc : mcs ){
                if (!CONSTRUCT_METHOD_NAME.equals(mc.methodIdentify.methodName) && mc.config.inherited ){
                    res.add(mc);
                }
            }
        }
        return res;
    }

}
