package com.lch.route.plugin.utils

import com.lch.route.plugin.PluginImpl
import com.lch.route.plugin.util.Logg
import com.lch.route.plugin.visitor.RouteServiceVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.lang.reflect.Field
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

public class ModifyUtil implements Opcodes {


    public static byte[] modifyClasses(String className, byte[] srcByteCode) {
        Logg.i("modifyClasses className========" + className);


        try {

            List<ClassVisitor> visitors = PluginImpl.INSTANCE.transform.visitors;
            Logg.e("!!!!!!!!!!!!!!!!!!!!!!!!!!!!=" + visitors.size());

            for (ClassVisitor visitor : visitors) {

                ClassReader reader = new ClassReader(srcByteCode);
                ClassWriter writer = new ClassWriter(reader, 0);

                setParentVisitor(visitor, writer)
                reader.accept(visitor, ClassReader.EXPAND_FRAMES)

                srcByteCode = writer.toByteArray();
            }

            ClassReader reader = new ClassReader(srcByteCode);
            ClassWriter writer = new ClassWriter(reader, 0);

            ClassVisitor visitor = new RouteServiceVisitor(ASM5, writer);
            reader.accept(visitor, ClassReader.EXPAND_FRAMES)
            srcByteCode = writer.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return srcByteCode;
    }

    private static void setParentVisitor(ClassVisitor visitor, ClassVisitor parent) {
        try {
            Field cv_field = ClassVisitor.class.getDeclaredField("cv");
            cv_field.setAccessible(true);
            cv_field.set(visitor, parent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static File modifyJar(File jarFile, File tempDir, boolean nameHex) {

        def inputJar = new JarFile(jarFile);
        def hexName = "";
        def namePrefix = "";

        if (nameHex) {
            int start = jarFile.absolutePath.indexOf("com.");
            if (start != -1) {
                int end = jarFile.absolutePath.indexOf(File.separator, start);
                if (end != -1) {
                    namePrefix = jarFile.absolutePath.subSequence(start, end);
                }
            }
            hexName = namePrefix + "_" + DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8);
        }

        def outputJar = new File(tempDir, hexName + jarFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar));

        Enumeration enumeration = inputJar.entries();

        while (enumeration.hasMoreElements()) {

            JarEntry jarEntry = (JarEntry) enumeration.nextElement();
            InputStream inputStream = inputJar.getInputStream(jarEntry);

            String entryName = jarEntry.getName();
            String className;

            ZipEntry zipEntry = new ZipEntry(entryName);

            jarOutputStream.putNextEntry(zipEntry);

            byte[] modifiedClassBytes = null;
            byte[] sourceClassBytes = IOUtils.toByteArray(inputStream);
            inputStream.close();

            if (entryName.endsWith(".class")) {
                className = path2Classname(entryName)
                modifiedClassBytes = modifyClasses(className, sourceClassBytes);
            }

            if (modifiedClassBytes == null) {
                jarOutputStream.write(sourceClassBytes);
            } else {
                jarOutputStream.write(modifiedClassBytes);
            }

            jarOutputStream.closeEntry();

        }


        jarOutputStream.close();
        inputJar.close();

        return outputJar;
    }

    public static String path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }
}