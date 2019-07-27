package pers.wengzc;


import android.util.Log;

import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.api.AndroidBasePlugin;
import com.android.build.gradle.api.AndroidSourceDirectorySet;
import com.android.build.gradle.api.AndroidSourceSet;
import com.android.build.gradle.internal.api.DefaultAndroidSourceSet;
import com.android.builder.model.AndroidProject;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.gradle.platform.base.BinaryTasks;

import java.io.File;
import java.util.Set;

import groovy.lang.Closure;

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
            System.out.println("MyPlugin apply(Project project),e="+e);
        }
    }
}


