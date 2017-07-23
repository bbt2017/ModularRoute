package com.lch.demo;

import org.objectweb.asm.ClassVisitor;

/**
 * Created by lichenghang on 2017/7/24.
 */

public class Jvi extends ClassVisitor {

    public Jvi(int api) {
        super(api);
    }

    public Jvi(int api, ClassVisitor cv) {
        super(api, cv);
    }
}
