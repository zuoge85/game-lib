package com.isnowfox.core.io;

import com.isnowfox.core.io.MarkCompressInput.Item;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class MarkCompressUtilsTool {
    public static final void main(String args[]) throws Exception {

        byte[] array = FileUtils.readFileToByteArray(new File("D:/tr/code/server/project/zgame-server/src/main/resources/map/data/test.bo.org"));

        InputStream inputStream = new ByteArrayInputStream(array);
        MarkCompressInput in = (MarkCompressInput) MarkCompressInput.create(inputStream);
        ArrayList<Item> map = in.readAll();
        System.out.println(map);
    }
}
