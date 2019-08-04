package pers.wengzc.snailhunter;

import com.android.build.gradle.BaseExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        try{
            //将transform接口实现绑定到androidPlugin 实现class文件字节码操作
            BaseExtension androidExtension = (BaseExtension) project.getExtensions().getByName("android");
            MyTransform myTransform = new MyTransform(project);
            androidExtension.registerTransform(myTransform);

            //配置值对象
            project.getExtensions().create("sh", ScriptConfigVal.class);
            ScriptConfigVal configVal = (ScriptConfigVal) project.getExtensions().getByName("sh");
            myTransform.setConfigVal(configVal);

        }catch (Exception e){
            System.out.println("SnailHunter Plugin 应用失败");
            e.printStackTrace();
        }
    }
}


