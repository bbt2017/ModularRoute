package com.lch.demo.aop;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by lichenghang on 2017/7/24.
 */

public class TimeLogVisitor extends ClassVisitor implements Opcodes {

    private String classname;

    public TimeLogVisitor(int api) {
        super(api);
    }

    public TimeLogVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classname = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor old = super.visitMethod(access, name, desc, signature, exceptions);

        if (classname.equals("com/lch/demo/aop/hooker/TimeLog")) {
            return old;
        }

        return new LogMethodVisitor(api, old, access, name, desc);
    }


    private static class LogMethodVisitor extends AdviceAdapter {


        public LogMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter();

            super.visitMethodInsn(INVOKESTATIC, "com/lch/demo/aop/hooker/TimeLog", "startTime", "()V", false);

        }

        @Override
        protected void onMethodExit(int opcode) {
            super.onMethodExit(opcode);

            super.visitMethodInsn(INVOKESTATIC, "com/lch/demo/aop/hooker/TimeLog", "endTime", "()V", false);
        }
    }


}
