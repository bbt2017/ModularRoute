package com.lch.route.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.lch.route.plugin.util.Logg
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.objectweb.asm.Opcodes

class PluginImpl implements Plugin<Project>, Opcodes {

    public static final int TYPE_APP = 1;
    public static final int TYPE_LIB = 2;

    public static PluginImpl INSTANCE;


    public Project mProject;

    public InjectTransform transform

    public int projectType

    public File routeDir

    public File routeTempDir

    @Override
    void apply(Project project) {
        Logg.i(":applied RoutePlugin===================================");
        INSTANCE = this;

        mProject = project

        BaseExtension plugin = project.extensions.getByType(BaseExtension)

        if (!(plugin instanceof AppExtension)) {
            throw new IllegalStateException("'android-application' plugin required.")
        }
        project.extensions.create('LchAop', PluginExtension)

        transform = new InjectTransform()
        plugin.registerTransform(transform)

        initDir(project);

        detectProjectType()
    }


    private  void detectProjectType(BaseExtension plugin) {

        if (plugin instanceof LibraryExtension) {
            projectType = TYPE_LIB;
        } else if (plugin instanceof AppExtension) {
            projectType = TYPE_APP;
        } else {
            projectType = -1
        }

    }


    private  void initDir(Project project) {

        File routeDir = new File(project.buildDir, "PluginImpl")
        if (!routeDir.exists()) {
            routeDir.mkdir()
        }
        File tempDir = new File(routeDir, "temp")
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        this.routeDir = routeDir
        this.routeTempDir = tempDir
    }


}
