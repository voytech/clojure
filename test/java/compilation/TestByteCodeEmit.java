package compilation;

import clojure.asm.ClassVisitor;
import clojure.asm.ClassWriter;
import clojure.asm.Opcodes;
import clojure.asm.Type;
import clojure.asm.commons.Method;
import clojure.lang.DynamicClassLoader;
import org.junit.Test;

/**
 * Created by voytech on 12.03.16.
 */
public class TestByteCodeEmit {
    @Test
    public void testDefineClass(){
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = cw;
        cv.visit(Opcodes.V1_5,Opcodes.ACC_PUBLIC,"MyTest",null,"clojure/lang/AFunction",null);
        cv.visitEnd();
        byte[] bytecode = cw.toByteArray();
        DynamicClassLoader classLoader = new DynamicClassLoader();
        Class clazz = classLoader.defineClass("MyTest",bytecode,"");
        try {
            Object instantiated = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //Type[] params = null;
        //Method m = new Method("testMethod", Type.VOID,params);
    }
}
