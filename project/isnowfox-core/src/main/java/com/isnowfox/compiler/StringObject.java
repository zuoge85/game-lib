package com.isnowfox.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;

/**
 * @author zuoge85 on 2014/12/15.
 */
public class StringObject extends SimpleJavaFileObject {
    private StringBuilder contents = null;

    public StringObject(String name, StringBuilder contents) throws Exception {
        super(StringCompilerUtils.url(name, Kind.SOURCE), Kind.SOURCE);
        this.contents = contents;
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors)
            throws IOException {
        return contents;
    }
}
