package com.yx.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class PreMain {

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new DefineTransformer());
    }

    static class DefineTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//            return ClassFileTransformer.super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
//            System.out.println("预加载类：" + className);

            //
            if ("com/yx/byagent/Main".equals(className)) {
                try {
                    ClassPool aDefault = ClassPool.getDefault();
                    CtClass mainClass = aDefault.get("com.yx.byagent.Main");
                    CtMethod main = mainClass.getDeclaredMethod("main");
                    String content = "{System.out.println(\"改了改了\");}";
                    main.setBody(content);
                    byte[] bytes = mainClass.toBytecode();
                    mainClass.detach();
                    return bytes;
                } catch (NotFoundException | CannotCompileException | IOException e) {
                    e.printStackTrace();
                }

            }

            return classfileBuffer;
        }
    }

}
