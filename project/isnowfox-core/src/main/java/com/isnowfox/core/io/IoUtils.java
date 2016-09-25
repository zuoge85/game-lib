package com.isnowfox.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zuoge85 on 2014/12/6.
 */
public final class IoUtils {
    public static int getBigEndianInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
}
