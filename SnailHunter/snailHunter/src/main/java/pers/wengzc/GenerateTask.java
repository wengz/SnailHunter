package pers.wengzc;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

public class GenerateTask extends DefaultTask {

    private File generateDir;

    @TaskAction
    public void greet (){
        Generator.generateCode(generateDir);
    }

    public File getGenerateDir() {
        return generateDir;
    }

    public void setGenerateDir(File generateDir) {
        this.generateDir = generateDir;
    }

}
