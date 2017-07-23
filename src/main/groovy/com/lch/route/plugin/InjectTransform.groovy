package com.lch.route.plugin

import com.android.annotations.NonNull
import com.android.annotations.Nullable
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.lch.route.plugin.util.Logg
import com.lch.route.plugin.utils.Config
import com.lch.route.plugin.utils.ModifyUtil
import com.lch.route.plugin.visitor.Router_Dump
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils

public class InjectTransform extends Transform {

    @Override
    String getName() {
        return "Route"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        if (Config.ext.projectType == Config.TYPE_APP) {
            return TransformManager.SCOPE_FULL_PROJECT
        } else if (Config.ext.projectType == Config.TYPE_LIB) {
            return TransformManager.SCOPE_FULL_LIBRARY
        }
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }


    @Override
    public void transform(
            @NonNull Context context,
            @NonNull Collection<TransformInput> inputs,
            @NonNull Collection<TransformInput> referencedInputs,
            @Nullable TransformOutputProvider outputProvider,
            boolean isIncremental) throws IOException, TransformException, InterruptedException {

        Logg.i("==============route transform enter==============")

        def classPaths = []

        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                classPaths.add(directoryInput.file.absolutePath)

            }

            input.jarInputs.each { JarInput jarInput ->
                classPaths.add(jarInput.file.absolutePath)

            }
        }

        def paths = [Config.project.android.bootClasspath.get(0).absolutePath/*, injectClassPath*/]
        paths.addAll(classPaths)


        Config.initVisitors(paths)


        long begin = System.currentTimeMillis();

        inputs.each { TransformInput input ->

            /**
             * 遍历jar。
             */
            input.jarInputs.each { JarInput jarInput ->

                String jarName = jarInput.file.name;
                String jar_absolutePath = jarInput.file.absolutePath;
                Logg.i("jar:" + jar_absolutePath);

                /** 重名名输出文件,因为可能同名,会覆盖*/
                def hexName = DigestUtils.md5Hex(jar_absolutePath).substring(0, 8);
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4);
                }
                /** 获得输出文件*/
                File dest = outputProvider.getContentLocation(jarName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR);

                def modifiedJar = null;

                if (isNeedModifyJar(jarInput.file)) {
                    modifiedJar = modifyJarFile(jarInput.file, context.getTemporaryDir());
                } else {
                    Logg.e("exclude jar:" + jar_absolutePath)
                }

                if (modifiedJar == null) {
                    FileUtils.copyFile(jarInput.file, dest);
                } else {
                    saveModifiedJarForCheck(modifiedJar);
                    FileUtils.copyFile(modifiedJar, dest);
                }


            }
            /**
             * 遍历目录
             */
            input.directoryInputs.each { DirectoryInput directoryInput ->

                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                File dir = directoryInput.file
                Logg.i("dir:" + dir.absolutePath);


                if (dir) {

                    HashMap<String, File> modifyMap = new HashMap<>();
                    dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                        File classFile ->
                            File modified = modifyClassFile(dir, classFile, context.getTemporaryDir());
                            if (modified != null) {
                                //key为相对路径
                                modifyMap.put(classFile.absolutePath.replace(dir.absolutePath, ""), modified);
                            }
                    }


                    byte[] bytes = Router_Dump.generateRoute_Class();
                    if (bytes != null) {
                        FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes), new File(dest, "com/lch/route/Route_.class"))
                    }

                    FileUtils.copyDirectory(directoryInput.file, dest);

                    modifyMap.entrySet().each {
                        Map.Entry<String, File> en ->
                            File target = new File(dest.absolutePath + en.getKey());
                            Logg.i(target.getAbsolutePath());
                            if (target.exists()) {
                                target.delete();
                            }

                            FileUtils.copyFile(en.getValue(), target);
                            saveModifiedJarForCheck(en.getValue());
                            en.getValue().delete();
                    }
                }
            }

            Logg.e("@@@transform spend time:" + (System.currentTimeMillis() - begin));

        }
    }

    private static boolean isNeedModifyJar(File jar) {

        List<String> excludeJarName = Config.project.route.excludeJarName;

        for (String value : excludeJarName) {
            boolean isContain = jar.absolutePath.contains(value);

            if (isContain) {
                return false;
            }
        }


        return true;
    }


    private static void saveModifiedJarForCheck(File optJar) {
        File dir = Config.ext.routeDir;
        File checkJarFile = new File(dir, optJar.getName());
        if (checkJarFile.exists()) {
            checkJarFile.delete();
        }
        FileUtils.copyFile(optJar, checkJarFile);
    }


    private static File modifyJarFile(File jarFile, File tempDir) {
        if (jarFile) {
            return ModifyUtil.modifyJar(jarFile, tempDir, true)

        }
        return null;
    }


    private static File modifyClassFile(File dir, File classFile, File tempDir) {
        File modified = null;
        try {
            String className = ModifyUtil.path2Classname(classFile.absolutePath.replace(dir.absolutePath + File.separator, ""));
            byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(classFile));

            byte[] modifiedClassBytes = ModifyUtil.modifyClasses(className, sourceClassBytes);
            if (modifiedClassBytes) {
                modified = new File(tempDir, className.replace('.', '') + '.class')
                if (modified.exists()) {
                    modified.delete();
                }

                modified.createNewFile()
                FileOutputStream fos = new FileOutputStream(modified);
                fos.write(modifiedClassBytes)
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return modified;

    }
}
