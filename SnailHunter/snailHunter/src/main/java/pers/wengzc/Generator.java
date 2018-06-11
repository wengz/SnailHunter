package pers.wengzc;

import android.os.Looper;

import com.squareup.javawriter.JavaWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.EnumSet;

import javax.lang.model.element.Modifier;

public class Generator {

    public static void generateCode (File dir){
        try{
            String packageName = "pers.wengzc";
            String className = "AndroidUtil";

            File outFile = new File(dir, packageName.replaceAll("\\.", "/")+"/"+className+".java");
            if (!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs();
            }
            if (!outFile.exists()){
                outFile.createNewFile();
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile));
            JavaWriter javaWriter = new JavaWriter(outputStreamWriter);
            javaWriter.emitPackage(packageName)
                    .emitImports(Looper.class)
                    .beginType(packageName+"."+className, "class", EnumSet.of(Modifier.PUBLIC))

                    .beginMethod("boolean", "isInUIThread", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC))
                    .emitStatement("return Looper.getMainLooper() == Looper.myLooper()")
                    .endMethod()
                    .endType()
                    .close();
        }catch (Exception e){
            System.out.println("pers.wengzc.AndroidUtil.java 类文件生成异常,e="+e);
        }

    }
}
