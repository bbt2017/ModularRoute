package com.lch.route.plugin.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;
import java.util.Set;

public class Router_Dump implements Opcodes {

    public static byte[] generateRoute_Class() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_SUPER, "com/lch/route/Route_", null, "java/lang/Object", null);

        cw.visitSource("Route_.java", null);

        {
            fv = cw.visitField(ACC_PRIVATE + ACC_STATIC, "SV", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;", null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "<init>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(9, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lcom/lch/route/Route_;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC, "getService", "(Ljava/lang/String;)Ljava/lang/Object;", "<T:Ljava/lang/Object;>(Ljava/lang/String;)TT;", null);
            mv.visitCode();
            Label l0 = new Label();
            Label l1 = new Label();
            Label l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(19, l3);
            mv.visitFieldInsn(GETSTATIC, "com/lch/route/Route_", "SV", "Ljava/util/Map;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "java/lang/String");
            mv.visitVarInsn(ASTORE, 1);
            mv.visitLabel(l0);
            mv.visitLineNumber(22, l0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "newInstance", "()Ljava/lang/Object;", false);
            mv.visitLabel(l1);
            mv.visitInsn(ARETURN);
            mv.visitLabel(l2);
            mv.visitLineNumber(24, l2);
            mv.visitFrame(Opcodes.F_FULL, 2, new Object[]{"java/lang/String", "java/lang/String"}, 1, new Object[]{"java/lang/Exception"});
            mv.visitVarInsn(ASTORE, 2);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(25, l4);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(27, l5);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l4, l5, 2);
            mv.visitLocalVariable("svName", "Ljava/lang/String;", null, l3, l6, 0);
            mv.visitLocalVariable("impl", "Ljava/lang/String;", null, l0, l6, 1);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE + ACC_STATIC, "put", "(Ljava/lang/String;Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(31, l0);
            mv.visitFieldInsn(GETSTATIC, "com/lch/route/Route_", "SV", "Ljava/util/Map;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            mv.visitInsn(POP);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(32, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("key", "Ljava/lang/String;", null, l0, l2, 0);
            mv.visitLocalVariable("val", "Ljava/lang/String;", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(11, l0);
            mv.visitTypeInsn(NEW, "java/util/HashMap");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            mv.visitFieldInsn(PUTSTATIC, "com/lch/route/Route_", "SV", "Ljava/util/Map;");


            Set<Map.Entry<String, String>> set = RouteServiceVisitor.sv.entrySet();

            for (Map.Entry<String, String> entry : set) {

                mv.visitLdcInsn(entry.getKey());
                mv.visitLdcInsn(entry.getValue());
                mv.visitMethodInsn(INVOKESTATIC, "com/lch/route/Route_", "put", "(Ljava/lang/String;Ljava/lang/String;)V", false);
            }


            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(15, l1);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 0);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();


    }
}