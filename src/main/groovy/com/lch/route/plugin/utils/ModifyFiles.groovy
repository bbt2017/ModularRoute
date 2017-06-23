package com.lch.route.plugin.utils

import com.lch.route.plugin.ModifyClassUtil
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


public class ModifyFiles {


    public static void modify(Map<String, Map<String, Object>> taskMap) {
        final File tempDir = DataHelper.ext.hiBeaverTempDir;
        taskMap.entrySet().forEach({
            entry ->
                String path = entry.getKey();
                Map<String, Object> map = entry.getValue();
                Util.initTargetClasses(map)
                File targetFile = new File(path)
                def type = isSupportFile(targetFile)
                switch (type) {
                    case Const.TY_AAR:
                        modifyAar(targetFile, map);
                        break;
                    case Const.TY_JAR:
                        File outJar = modifyJar(targetFile, map, tempDir, false);
                        outJar.renameTo(new File(DataHelper.ext.hiBeaverDir, outJar.getName()))
                        break;
                }
        })
    }

    public static File unzipEntryToTemp(ZipEntry element, ZipFile zipFile) {
        def stream = zipFile.getInputStream(element);
        def array = IOUtils.toByteArray(stream);
        String hex = DigestUtils.md5Hex(element.getName());
        final File tempDir = DataHelper.ext.hiBeaverTempDir;
        File targetFile = new File(tempDir, hex + ".jar");
        if (targetFile.exists()) {
            targetFile.delete()
        }
        new FileOutputStream(targetFile).write(array)
        return targetFile
    }

    public static File modifyJar(File jarFile, Map<String, Object> modifyMatchMaps, File tempDir, boolean nameHex) {

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
                className = Util.path2Classname(entryName)
                // String key = Util.shouldModifyClass(className)
                // if (modifyMatchMaps != null /*&& key != null*/) {
                modifiedClassBytes = ModifyClassUtil.modifyClasses(className, sourceClassBytes);
                //}
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

    public static void modifyAar(File targetFile, Map<String, Object> map) {
        final File hiBeaverDir = DataHelper.ext.hiBeaverDir;
        final File tempDir = DataHelper.ext.hiBeaverTempDir;
        ZipFile zipFile = new ZipFile(targetFile);
        Enumeration<ZipEntry> entries = zipFile.entries();

        def outputAar = new File(hiBeaverDir, targetFile.name)
        if (outputAar.exists()) {
            outputAar.delete()
        }

        ZipOutputStream outputAarStream = new ZipOutputStream(new FileOutputStream(outputAar))
        while (entries.hasMoreElements()) {
            ZipEntry element = entries.nextElement();
            def name = element.getName();
            ZipEntry zipEntry = new ZipEntry(name);

            outputAarStream.putNextEntry(zipEntry);
            Log.info("name is ${name}")
            if (name.endsWith(".jar")) {
                File innerJar = unzipEntryToTemp(element, zipFile);
                def outJar = modifyJar(innerJar, map, tempDir, true);
                outputAarStream.write(IOUtils.toByteArray(new FileInputStream(outJar)))
            } else {
                def stream = zipFile.getInputStream(element)
                byte[] array = IOUtils.toByteArray(stream)
                Log.info("length is ${array.length}")
                if (array != null) {
                    outputAarStream.write(array)
                }
            }
            outputAarStream.closeEntry();
        }
        zipFile.close()
        outputAarStream.close()
    }

    public static int isSupportFile(File targetFile) {
        def name = targetFile.getName();
        if (name.endsWith(".jar")) {
            return Const.TY_JAR;
        } else if (name.endsWith(".aar")) {
            return Const.TY_AAR;
        }
        return -1;
    }
}