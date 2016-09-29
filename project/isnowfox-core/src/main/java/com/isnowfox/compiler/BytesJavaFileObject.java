package com.isnowfox.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zuoge85 on 2014/12/15.
 */
public class BytesJavaFileObject extends SimpleJavaFileObject {
    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public BytesJavaFileObject(String name, Kind kind) {
        super(StringCompilerUtils.url(name, kind), kind);
    }


    @Override
    public OutputStream openOutputStream() throws IOException {
        return out;
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }
}
