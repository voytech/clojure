package compilation;

import clojure.lang.LineNumberingPushbackReader;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import clojure.lang.LispReader;

import java.io.Reader;

/**
 * Created by voytech on 06.03.16.
 * I just want to play with clojure pushback reader.
 * Just a testing of how clojure homoiconic property is conducted.
 */
public class TestLispReader {
    @Test
    public void testLispReader01(){
        LispReader reader;
        System.out.println();
        Reader rdr = null;
        LineNumberingPushbackReader pushbackReader = (rdr instanceof LineNumberingPushbackReader) ? (LineNumberingPushbackReader) rdr : new LineNumberingPushbackReader(rdr);
        //for(Object r = LispReader.read(pushbackReader, false, EOF, false, readerOpts); r != EOF;
		//r = LispReader.read(pushbackReader, false, EOF, false, readerOpts))
		//{
		//	LINE_AFTER.set(pushbackReader.getLineNumber());
		//	COLUMN_AFTER.set(pushbackReader.getColumnNumber());
		//	compile1(gen, objx, r);
		//	LINE_BEFORE.set(pushbackReader.getLineNumber());
		//	COLUMN_BEFORE.set(pushbackReader.getColumnNumber());
		//}
    }
}
