package compilation;

import clojure.asm.ClassVisitor;
import clojure.asm.ClassWriter;
import clojure.asm.Opcodes;
import clojure.asm.Type;
import clojure.asm.commons.GeneratorAdapter;
import clojure.asm.commons.Method;
import clojure.lang.DynamicClassLoader;
import org.junit.Test;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by voytech on 12.03.16.
 */
public class TestByteCodeEmit {
    public static final DynamicClassLoader classloader = new DynamicClassLoader();;

    private Class genSampleClass(String name,int idx){
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = cw;
        cv.visit(Opcodes.V1_5,Opcodes.ACC_PUBLIC,name+idx,null,"java/lang/Object",null);
        Method voidctor = Method.getMethod("void <init>()");
		GeneratorAdapter ctorgen = new GeneratorAdapter(Opcodes.ACC_PUBLIC,
		                                                voidctor,
		                                                null,
		                                                null,
		                                                cv);
        ctorgen.visitCode();
        ctorgen.loadThis();

        ctorgen.invokeConstructor(Type.getType(java.lang.Object.class),voidctor);
        ctorgen.getStatic(Type.getType(java.lang.System.class),"out",Type.getType(PrintStream.class));
        ctorgen.push("Hello From Dynamic ByteCode! [ "+name+idx+" ]");
        ctorgen.invokeVirtual(Type.getType(PrintStream.class),Method.getMethod("void println(String)"));
        ctorgen.returnValue();
        ctorgen.endMethod();
        cv.visitEnd();
        byte[] bytecode = cw.toByteArray();
       // DynamicClassLoader classloader = new DynamicClassLoader();
        Class clazz = classloader.defineClass(name+idx,bytecode,"");
        try {
            Object instantiated = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testDefineClass(){
        int  corePoolSize  =    5;
        int  maxPoolSize   =   10;
        long keepAliveTime = 5000;
        int target_class_count = 10000;
        int units_count = 1;
        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        class CompilationUnit implements Callable<Long> {
            private final int start;
            private final int end;
            private final String unit_id;
            public CompilationUnit(String unit_id,int start,int end){
                this.start = start;
                this.end = end;
                this.unit_id = unit_id;
            }

            @Override
            public Long call() throws Exception {
                long units = System.currentTimeMillis();
                for (int i=start;i<end;i++){
                    genSampleClass("MyTest "+unit_id+" ",i);
                }
                long end = System.currentTimeMillis() - units;
                return end;
            }
        }
        int j=0;
        long tstart = System.currentTimeMillis();
        List<CompilationUnit> units = new ArrayList<CompilationUnit>();
        int part = target_class_count / units_count;
        for (int i=0;i<units_count;i++,j+=part){
            units.add(new CompilationUnit(" Unit-"+i,j,j+part));
        }
        System.out.println("Total time spend in milis: "+(System.currentTimeMillis()-tstart));
        try {
            for (Future<Long> result : threadPoolExecutor.invokeAll(units)){
                try {
                    System.out.println("Took time:"+result.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
