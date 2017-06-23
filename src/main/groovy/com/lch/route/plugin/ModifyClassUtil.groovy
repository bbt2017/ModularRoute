package com.lch.route.plugin

import com.lch.route.plugin.util.Logg
import com.lch.route.plugin.visitor.RouteServiceVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

public class ModifyClassUtil implements Opcodes {


    public static byte[] modifyClasses(String className, byte[] srcByteCode) {
        Logg.i("className========" + className);

        try {
            ClassReader reader = new ClassReader(srcByteCode);
            ClassWriter writer = new ClassWriter(reader, 0);
            ClassVisitor visitor = new RouteServiceVisitor(Opcodes.ASM5, writer);
            reader.accept(visitor, ClassReader.EXPAND_FRAMES)

            srcByteCode = writer.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcByteCode;
    }


}