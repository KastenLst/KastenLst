package com.kastenlst;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ScriptLoader extends ClassLoader {
    private final File classPath;
    public ScriptLoader(File classPath) {
        this.classPath = classPath;
    }
    protected Class<?> findClass(String name) 
     throws ClassNotFoundException {
        try {
            FileInputStream fis = new FileInputStream(new File(classPath, name + ".class"));
            byte[] classBytes = fis.readAllBytes();
            fis.close();
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Class not found", e); 
        }
    }
}