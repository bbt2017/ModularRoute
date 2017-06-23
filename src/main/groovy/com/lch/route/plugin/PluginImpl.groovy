package com.lch.route.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.lch.route.plugin.util.Logg
import com.lch.route.plugin.utils.DataHelper
import com.lch.route.plugin.utils.Util
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Logg.i(":applied RoutePlugin===================================");
        BaseExtension android = project.extensions.getByType(BaseExtension)

        if (!(android instanceof AppExtension)) {
            throw new IllegalStateException("'android-application' plugin required.")
        }
        project.extensions.create('route', PluginExtension)


        Util.setProject(project);
        registerTransform(project);
        initDir(project);
    }


    private static registerTransform(Project project) {
        BaseExtension android = project.extensions.getByType(BaseExtension)
        if (android instanceof LibraryExtension) {
            DataHelper.ext.projectType = DataHelper.TYPE_LIB;
        } else if (android instanceof AppExtension) {
            DataHelper.ext.projectType = DataHelper.TYPE_APP;
        } else {
            DataHelper.ext.projectType = -1
        }
        InjectTransform transform = new InjectTransform()
        android.registerTransform(transform)
    }


    private static void initDir(Project project) {
        File hiBeaverDir = new File(project.buildDir, "PluginImpl")
        if (!hiBeaverDir.exists()) {
            hiBeaverDir.mkdir()
        }
        File tempDir = new File(hiBeaverDir, "temp")
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        DataHelper.ext.hiBeaverDir = hiBeaverDir
        DataHelper.ext.hiBeaverTempDir = tempDir
    }


}
