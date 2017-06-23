package com.lch.route.plugin.visitor;

import com.lch.route.plugin.util.Logg;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lichenghang on 2017/5/28.
 */

public class RouteServiceVisitor extends ClassVisitor implements Opcodes {

    public static Map<String, String> sv = new HashMap<>();

    private String classname;


    public RouteServiceVisitor(int api) {
        super(api);
    }

    public RouteServiceVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classname = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        AnnotationVisitor old = super.visitAnnotation(desc, visible);

        return new RouteAnnotationVisitor(ASM5, old, desc);

    }

    private class RouteAnnotationVisitor extends AnnotationVisitor {

        private static final String ROUTE_SERVICE_ANNO_DESC = "Lcom/lch/route/RouteServiceAnnotation;";

        private String desc;

        public RouteAnnotationVisitor(int api) {
            super(api);
        }

        public RouteAnnotationVisitor(int api, AnnotationVisitor av, String desc) {
            super(api, av);
            this.desc = desc;
        }

        @Override
        public void visit(String name, Object value) {

            if (desc.equals(ROUTE_SERVICE_ANNO_DESC) && name.equals("serviceInterface")) {
                Logg.e("visit annotation:" + name + "#" + value + "#" + desc + "#" + classname);

                sv.put(value.toString(), classname.replaceAll("/", "."));
            }

            super.visit(name, value);
        }


    }
}
