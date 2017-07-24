package com.lch.demo.aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class TestVisitor extends ClassVisitor {

    String className;

    public TestVisitor(int i) {
        super(i);
    }

    public TestVisitor(int i, ClassVisitor classVisitor) {
        super(i, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.err.print("TestVisitor visit:" + name);
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor old = super.visitMethod(access, name, desc, signature, exceptions);

        if (!className.contains("MainActivity") || !name.equals("test")) {
            return old;
        }

        System.err.print("*************************** visit:" + className + ";" + name);

        return new CtrMethodVisitor(org.objectweb.asm.Opcodes.ASM5, old);

    }

    private static class CtrMethodVisitor extends MethodVisitor {

        public CtrMethodVisitor(int api) {
            super(api);
        }

        public CtrMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            super.visitLdcInsn("tag");
            super.visitLdcInsn("xxxxj");
            // 下面这行代码 为要调用的方法，请酌情修改
            super.visitMethodInsn(org.objectweb.asm.Opcodes.INVOKESTATIC,
                    "com/lch/demo/MainActivity",
                    "hookXM", "(Ljava/lang/Object;Ljava/lang/Object;)V");


        }


    }
}