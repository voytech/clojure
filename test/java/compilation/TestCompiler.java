package compilation;
import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.Var;
import junit.framework.Assert;
import org.junit.Test;
import clojure.lang.Compiler;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
/**
 * Created by voytech on 09.03.16.
 * I will be debugging compilation here.
 */
public class TestCompiler {

    final static private Symbol CLOJURE_MAIN = Symbol.intern("clojure.main");
    final static private Var REQUIRE = RT.var("clojure.core", "require");
    final static private Var LEGACY_REPL = RT.var("clojure.main", "legacy-repl");
    final static private Var LEGACY_SCRIPT = RT.var("clojure.main", "legacy-script");
    final static private Var MAIN = RT.var("clojure.main", "main");

    public static final String cljcode_01 = "" +
            "(ns clj.compiler.testing) " +
            "(def myfunc " +
            "   (fn [x y] " +
            "     (fn [z] "+
            "       (do " +
            "           (println (str z \" + \" x \" + \" y))" +
            "           (+ x y z)))))" +
            "(myfunc 2 4)";
    @Test
    public void testCljCode01(){
        StringReader reader = new StringReader(cljcode_01);
        Object result = Compiler.load(reader);

    }

}
