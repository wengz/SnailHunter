package pers.wengzc;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
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
import java.io.FileInputStream;
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
import javassist.CtMethod;
import javassist.bytecode.AccessFlag;
import pers.wengzc.hunterKit.AndroidUtil;
import pers.wengzc.hunterKit.ExamineMethodRunTime;

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

            ClassPool classPool = new ClassPool();
            BaseExtension androidExtension = (BaseExtension) project.getExtensions().getByName("android");
            List<File> bootClassPath = androidExtension.getBootClasspath();
            for (File file : bootClassPath){
                //System.out.println("boot class path="+file.getAbsolutePath());
                try{
                    classPool.appendClassPath(file.getAbsolutePath());
                }catch (Exception e){
                    //System.out.println("boot class path append fail");
                    e.printStackTrace();
                }
            }

            List<CtClass> box = ConvertUtil.toCtClasses(inputs, classPool);
            insertCode(box, jarFile);

        }catch ( Exception e ){
            e.printStackTrace();
        }
    }

    private void insertCode (List<CtClass> box, File jarFile) throws Exception {
        ZipOutputStream outputStream = new JarOutputStream(new FileOutputStream(jarFile));
        for (CtClass ctClass : box){
            ctClass.setModifiers(AccessFlag.setPublic(ctClass.getModifiers()));

            if (
                    isNeedInsertClass(ctClass.getName())
                    &&
                    !( ctClass.isInterface() || ctClass.getDeclaredMethods().length < 1 )
            ){
                CtMethod[] ms = ctClass.getDeclaredMethods();
                if (ms != null){
                    for (CtMethod cm : ms){
                        System.out.println("ctmethod signature="+cm.getSignature());
                    }
                }
                zipFile(transformCode(ctClass.toBytecode(), ctClass.getName().replaceAll("\\.", "/")), outputStream, ctClass.getName().replaceAll("\\.", "/") + ".class");
            }else{
                zipFile(ctClass.toBytecode(), outputStream, ctClass.getName().replaceAll("\\.", "/") + ".class");
            }
        }
        outputStream.close();
    }

    private boolean isNeedInsertClass(String className) {
        if (className.contains("AndroidUtil")){
            return false;
        }

        if (className.contains("ViewMethodSignature")){
            return true;
        }

        return false;
//        if (className.contains("snailhunter")){
//            System.out.println("------- isNeedInsertClass -------->>>"+className);
//            return true;
//        }
//        return false;
    }

    private void zipFile (byte[] classBytesArray, ZipOutputStream zos, String entryName){
        try{
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            zos.write(classBytesArray, 0, classBytesArray.length);
            zos.closeEntry();;
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

            Iterator<MethodNode> it = classNode.methods.iterator();
            while (it.hasNext()){
                MethodNode mnd = it.next();
                System.out.println("转化方法 name="+mnd.name+" signature="+mnd.signature +" desc="+mnd.desc);
                transformMethod(mnd, classNode);
            }

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(cw);
            byte[] b = cw.toByteArray();
            return b;
        }catch (Exception e){
            System.out.println("class文件"+className+"修改失败,exception="+e);
            e.printStackTrace();
        }
        return bs;
    }



    private void recursiveTransform (File file){
        if (file.isDirectory()){
            File[] subFiles = file.listFiles();
            for (File f : subFiles){
                recursiveTransform(f);
            }
        }else if (file.isFile()){
            transformClass(file.getAbsolutePath());
        }
    }

    private void transformClass (String classFilePath) {
        String className = "";
        try{
            //System.out.println("---- transformClass ------ classFilePath="+classFilePath);
            FileInputStream fileInputStream = new FileInputStream(classFilePath);
            ClassReader classReader = new ClassReader(fileInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            className = classNode.name;
            if (util_class_name.equals(className)){
                return;
            }

            Iterator<MethodNode> it = classNode.methods.iterator();
            while (it.hasNext()){
                MethodNode mnd = it.next();
                transformMethod(mnd, classNode);
            }

            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(cw);
            byte[] b = cw.toByteArray();
            FileOutputStream fileOutputStream = new FileOutputStream(""+classFilePath);
            fileOutputStream.write(b, 0, b.length);
            fileOutputStream.close();
        }catch (Exception e){
            System.out.println("class文件"+className+"修改失败,exception="+e);
            e.printStackTrace();
        }
    }

    private static final String util_class_name = Type.getInternalName(AndroidUtil.class);

    private void transformMethod (MethodNode mnd, ClassNode cn) {

//        int orgLocalVarSize = mnd.maxLocals;
//        String methodName = mnd.name;
//        String className = cn.name;
//
//        boolean examineMethod = false;
//        List<AnnotationNode>annos = mnd.invisibleAnnotations;
//        if (annos != null && annos.size() > 0){
//            for (AnnotationNode anno : annos){
//                if (Type.getDescriptor(ExamineMethodRunTime.class).equals(anno.desc)){
//                    examineMethod = true;
//                }
//            }
//        }
//
//        //没有函数注解，没有开启主线程追踪无需注入代码
//        if (!examineMethod && !configVal.huntInMainThread){
//            return;
//        }
//
//        InsnList insnList = mnd.instructions;
//
//        if (insnList.size() < 1){
//            return;
//        }
//
//        InsnList codeInsertStart = new InsnList();
//        codeInsertStart.add(new LdcInsnNode(0L));
//        codeInsertStart.add(new VarInsnNode(Opcodes.LSTORE, orgLocalVarSize));
//        codeInsertStart.add(new VarInsnNode(Opcodes.LLOAD, orgLocalVarSize));
//        codeInsertStart.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
//                "java/lang/System",
//                "currentTimeMillis",
//                "()J", false));
//        codeInsertStart.add(new InsnNode(Opcodes.LSUB));
//        codeInsertStart.add(new VarInsnNode(Opcodes.LSTORE, orgLocalVarSize));
//
//        List<AbstractInsnNode> finishInsn = new ArrayList<AbstractInsnNode>();
//        Iterator<AbstractInsnNode> insnIt = insnList.iterator();
//        while (insnIt.hasNext()) {
//            AbstractInsnNode insn = insnIt.next();
//            if (FinishInsn.contains(insn.getOpcode())) {
//                finishInsn.add(insn);
//            }
//        }
//
//        insnList.insertBefore(insnList.getFirst(), codeInsertStart);
//
//        Iterator<AbstractInsnNode> finishInsnIt = finishInsn.iterator();
//        while (finishInsnIt.hasNext()){
//            AbstractInsnNode insn = finishInsnIt.next();
//
//            InsnList codeInsertEnd = new InsnList();
//            LabelNode end = new LabelNode();
//
//            if (!examineMethod){//仅主线程追踪
//                codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
//                        util_class_name,
//                        "isInUIThread",
//                        "()Z", false));
//                codeInsertEnd.add(new JumpInsnNode(Opcodes.IFEQ, end));
//            }
//
//            codeInsertEnd.add(new VarInsnNode(Opcodes.LLOAD, orgLocalVarSize));
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
//                    "java/lang/System",
//                    "currentTimeMillis",
//                    "()J", false));
//            codeInsertEnd.add(new InsnNode(Opcodes.LADD));
//            codeInsertEnd.add(new VarInsnNode(Opcodes.LSTORE, orgLocalVarSize));
//
//            if (!examineMethod){//仅主线程追踪
//                codeInsertEnd.add(new VarInsnNode(Opcodes.LLOAD, orgLocalVarSize));
//                codeInsertEnd.add(new LdcInsnNode(new Long(configVal.timeCriterion)));
//                codeInsertEnd.add(new InsnNode(Opcodes.LCMP));
//                codeInsertEnd.add(new JumpInsnNode(Opcodes.IFLE, end));
//            }
//
//            codeInsertEnd.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out",
//                    "Ljava/io/PrintStream;"));
//            codeInsertEnd.add(new TypeInsnNode(Opcodes.NEW, "java/lang/StringBuilder"));
//            codeInsertEnd.add(new InsnNode(Opcodes.DUP));
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false));
//
//            String hintStr = ( examineMethod ?
//                    "类[" + className+ "].函数[" + methodName + "]执行时间为(毫秒):"
//                        :
//                    "类[" + className+ "].函数[" + methodName + "]在UI线程中耗时较长,执行时间为(毫秒):"
//                        );
//            codeInsertEnd.add(new LdcInsnNode(hintStr));
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                    "java/lang/StringBuilder",
//                    "append",
//                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
//            codeInsertEnd.add(new VarInsnNode(Opcodes.LLOAD, orgLocalVarSize));
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                    "java/lang/StringBuilder",
//                    "append",
//                    "(J)Ljava/lang/StringBuilder;", false));
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                    "java/lang/StringBuilder",
//                    "toString",
//                    "()Ljava/lang/String;", false));
//
//            codeInsertEnd.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                    "java/io/PrintStream",
//                    "println",
//                    "(Ljava/lang/String;)V", false));
//            codeInsertEnd.add(end);
//
//            insnList.insertBefore(insn, codeInsertEnd);
//        }
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
