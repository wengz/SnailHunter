package pers.wengzc.snailhunter;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AccessFlag;
import pers.wengzc.hunterkit.Action;
import pers.wengzc.hunterkit.AndroidUtil;
import pers.wengzc.hunterkit.ByteCodeBridge;
import pers.wengzc.hunterkit.HunterTarget;

public class MyTransform extends Transform{

    private Project project;

    private ScriptConfigVal configVal;

    public MyTransform(Project project){
        this.project = project;
    }

    @Override
    public String getName() {
        return "SnailHunterTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        try{
            configVal.adjuestInternal();
            System.out.println(configVal.toString());

            Collection<TransformInput> inputs = transformInvocation.getInputs();
            TransformOutputProvider transformOutputProvider = transformInvocation.getOutputProvider();

            transformOutputProvider.deleteAll();
            File jarFile = transformOutputProvider.getContentLocation("main", getOutputTypes(), getScopes(), Format.JAR);

            if (!jarFile.getParentFile().exists()){
                jarFile.getParentFile().mkdirs();
            }
            if (jarFile.exists()){
                jarFile.delete();
            }

            ClassPool classPool = ClassPool.getDefault();
            BaseExtension androidExtension = (BaseExtension) project.getExtensions().getByName("android");
            List<File> bootClassPath = androidExtension.getBootClasspath();
            for (File file : bootClassPath){
                try{
                    classPool.appendClassPath(file.getAbsolutePath());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            List<CtClass> box = ConvertUtil.toCtClasses(inputs, classPool);
            insertCode(box, jarFile);

        }catch ( Exception e ){
            e.printStackTrace();
        }finally {
            AnnotationConfigHelper.releaseResource();
        }
    }

    private void insertCode (List<CtClass> box, File jarFile) throws Exception {
        ZipOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        for (CtClass ctClass : box){
            if (
                isNeedInsertClass(ctClass.getName())
                &&
                !( ctClass.isInterface() || ctClass.getDeclaredMethods().length < 1 )
            ){
                if (ctClass.isFrozen()) {
                    ctClass.defrost();
                }
                zipFile(transformCode(ctClass.toBytecode(), ctClass.getName()), outputStream, ctClass.getName().replaceAll("\\.", "/") + ".class");
            }else{
                zipFile(ctClass.toBytecode(), outputStream, ctClass.getName().replaceAll("\\.", "/") + ".class");
            }
        }
        outputStream.close();
    }

    private boolean isNeedInsertClass(String className) {
        if (className.startsWith("pers.wengzc.snailhunterrt")
            || className.startsWith("pers.wengzc.hunterkit")
            || className.startsWith("android")
            || className.startsWith("androidx")
            || className.startsWith("java")
            || className.startsWith("javax")){
            return false;
        }
        return true;
    }

    private void zipFile (byte[] classBytesArray, ZipOutputStream zos, String entryName){
        try{
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            zos.write(classBytesArray, 0, classBytesArray.length);
            zos.closeEntry();
            zos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private byte[] transformCode (byte[] bs, String className)throws Exception{
        try{
            ClassReader classReader = new ClassReader(bs);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            List<MethodConfig> classMethodConfig = AnnotationConfigHelper.getClassSelfMethodConfig(className);
            Iterator<MethodNode> it = classNode.methods.iterator();

            String packageName = AnnotationConfigHelper.getClassPackageName(className);
            ScriptConfigVal.ConfigItem classExcludeConfigItem = configVal.matchExclude(packageName, className, null);
            //类的脚本配置项排除
            if (classExcludeConfigItem == null){
                while (it.hasNext()){
                    MethodNode mnd = it.next();
                    String methodName = mnd.name;
                    ScriptConfigVal.ConfigItem methodExcludeConfigItem = configVal.matchExclude(packageName, className, methodName);
                    //方法的脚本配置项排除
                    if (methodExcludeConfigItem == null){
                        boolean matched = false;
                        for (MethodConfig mc : classMethodConfig){
                            if (mc.match(mnd)){
                                if (mc.config.action == Action.Include){
                                    System.out.println("---注解匹配成功! 进行字节码注入, 类名="+className+", 方法名="+mnd.name);
                                    transformMethod(packageName, classNode, mnd, mc.getMethodManipulateArg());
                                }
                                matched = true;
                                break;
                            }
                        }

                        //方法脚本配置项包含
                        if (!matched){
                            ScriptConfigVal.ConfigItem methodIncludeConfigItem = configVal.matchInclude(packageName, className, methodName);
                            if (methodIncludeConfigItem != null){
                                System.out.println("---脚本配置成功! 进行字节码注入, 类名="+className+", 方法名="+mnd.name);
                                transformMethod(packageName, classNode, mnd, methodIncludeConfigItem.getMethodManipulateArg());
                            }
                        }
                    }
                }
            }

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            byte[] b = cw.toByteArray();
            return b;
        }catch (Exception e){
            e.printStackTrace();
        }
        return bs;
    }

    private static final String util_class_name = Type.getInternalName(AndroidUtil.class);

    private void transformMethod (String packageName, ClassNode cn, MethodNode mnd, MethodManipulateArg manipulateArg) {

        int orgLocalVarSize = mnd.maxLocals;
        String methodName = mnd.name;
        String className = cn.name;
        className = className.replace("/", ".");

        InsnList insnList = mnd.instructions;
        //无指令直接返回
        if (insnList.size() < 1){
            return;
        }

        InsnList codeInsertStart = new InsnList();
        codeInsertStart.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "java/lang/System",
                "nanoTime",
                "()J", false));
        codeInsertStart.add(new VarInsnNode(Opcodes.LSTORE, orgLocalVarSize));

        List<AbstractInsnNode> finishInsn = new ArrayList<AbstractInsnNode>();
        Iterator<AbstractInsnNode> insnIt = insnList.iterator();
        while (insnIt.hasNext()) {
            AbstractInsnNode insn = insnIt.next();
            if (FinishInsn.contains(insn.getOpcode())) {
                finishInsn.add(insn);
            }
        }

        insnList.insertBefore(insnList.getFirst(), codeInsertStart);

        Iterator<AbstractInsnNode> finishInsnIt = finishInsn.iterator();
        while (finishInsnIt.hasNext()){
            AbstractInsnNode insn = finishInsnIt.next();

            InsnList codeInsertEnd = new InsnList();

            String byteCodeBrudgeInternalName = Type.getInternalName(ByteCodeBridge.class);
            codeInsertEnd.add(new LdcInsnNode(packageName));
            codeInsertEnd.add(new LdcInsnNode(className));
            codeInsertEnd.add(new LdcInsnNode(methodName));
            //开始时间
            codeInsertEnd.add(new VarInsnNode(Opcodes.LLOAD, orgLocalVarSize));
            //结束时间
            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    "java/lang/System",
                    "nanoTime",
                    "()J", false));
            codeInsertEnd.add(new LdcInsnNode(new Boolean(manipulateArg.justMainThread)));
            codeInsertEnd.add(new LdcInsnNode(new Long(manipulateArg.timeConstraint)));
            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    byteCodeBrudgeInternalName,
                    "handle",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZJ)V", false));

            insnList.insertBefore(insn, codeInsertEnd);
        }
    }

    public static List<Integer> FinishInsn = Arrays.asList(
            Opcodes.RETURN, Opcodes.ARETURN, Opcodes.DRETURN, Opcodes.FRETURN, Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.ATHROW );

    public ScriptConfigVal getConfigVal() {
        return configVal;
    }

    public void setConfigVal(ScriptConfigVal configVal) {
        this.configVal = configVal;
    }

}
