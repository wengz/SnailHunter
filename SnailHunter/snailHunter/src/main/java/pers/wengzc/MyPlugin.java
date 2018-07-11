package pers.wengzc;


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

    private static final String PreBuildTaskName = "preBuild";
    private static final String JavaGenerateTaskName = "SnailHunterJavaGenerateTask";
    private static final String JavaGenerateDir = "/SnailHunterGenerate";
    private static final String MainModuleName = "app";

    @Override
    public void apply(Project project) {
        try{
            //将transform接口实现绑定到androidPlugin 实现class文件字节码操作
            BaseExtension androidExtension = (BaseExtension) project.getExtensions().getByName("android");
            MyTransform myTransform = new MyTransform();
            androidExtension.registerTransform(myTransform);

            //配置值对象
            ScriptConfigVal configVal = project.getExtensions().create("snailHunter", ScriptConfigVal.class);
            myTransform.setConfigVal(configVal);

            //工具类只在主工程中生成
            if (MainModuleName.equals(project.getName())){
                //工具类java文件生成任务创建，并将其绑定到preBuild任务之前
                File buildDir = project.getBuildDir();
                project.getTasks().create(JavaGenerateTaskName, GenerateTask.class, new Action<GenerateTask>() {
                    @Override
                    public void execute(GenerateTask generateTask) {}
                });

                GenerateTask generateTask = (GenerateTask) project.getTasks().getByName(JavaGenerateTaskName);
                File myGenerateDir = new File(buildDir, JavaGenerateDir);
                generateTask.setGenerateDir(myGenerateDir);

                Task preBuildTask = project.getTasks().getByName(PreBuildTaskName);
                preBuildTask.dependsOn(generateTask);

                //将java生成文件所在的目录加入项目源码编译路径
                androidExtension.sourceSets(new Action<NamedDomainObjectContainer<AndroidSourceSet>>() {
                    @Override
                    public void execute(NamedDomainObjectContainer<AndroidSourceSet> androidSourceSets) {
                        DefaultAndroidSourceSet defaultAndroidSourceSet = (com.android.build.gradle.internal.api.DefaultAndroidSourceSet) androidSourceSets.maybeCreate("main");
                        AndroidSourceDirectorySet androidSourceDirectorySet = defaultAndroidSourceSet.getJava();
                        androidSourceDirectorySet.srcDir(myGenerateDir);
                    }
                });
            }

        }catch (Exception e){
            System.out.println("MyPlugin apply(Project project),e="+e);
        }
    }
}


