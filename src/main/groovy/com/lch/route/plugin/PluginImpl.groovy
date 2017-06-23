package com.lch.route.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.lch.route.plugin.util.Logg
import com.lch.route.plugin.utils.Config
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        Logg.i(":applied RoutePlugin===================================");
        BaseExtension plugin = project.extensions.getByType(BaseExtension)

        if (!(plugin instanceof AppExtension)) {
            throw new IllegalStateException("'android-application' plugin required.")
        }
        project.extensions.create('route', PluginExtension)

        Config.setProject(project);
        registerTransform(plugin);
        initDir(project);
    }


    private static registerTransform(BaseExtension plugin) {
        if (plugin instanceof LibraryExtension) {
            Config.ext.projectType = Config.TYPE_LIB;
        } else if (plugin instanceof AppExtension) {
            Config.ext.projectType = Config.TYPE_APP;
        } else {
            Config.ext.projectType = -1
        }
        InjectTransform transform = new InjectTransform()
        plugin.registerTransform(transform)
    }


    private static void initDir(Project project) {
        File routeDir = new File(project.buildDir, "PluginImpl")
        if (!routeDir.exists()) {
            routeDir.mkdir()
        }
        File tempDir = new File(routeDir, "temp")
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        Config.ext.routeDir = routeDir
        Config.ext.routeTempDir = tempDir
    }


}
