package com.isnowfox.core.io;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.Scanner;

public class ConsoleCommand {

    /**
     * 会阻塞当前线程
     */
    public static final void blockStart(Command cmd) {
        try (Scanner in = new Scanner(System.in)) {
            String line;
            while (null != (line = in.nextLine())) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] array = line.split("[\\s]+");
                    String name = array[0].trim();
                    if (array.length > 1) {
                        array = Arrays.copyOfRange(array, 1, array.length);
                    } else {
                        array = ArrayUtils.EMPTY_STRING_ARRAY;
                    }
                    boolean result;
                    try {
                        result = cmd.execute(name, array);
                    } catch (Throwable th) {
                        result = cmd.onError(th);
                    }
                    if (result) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * 非阻塞实现
     */
    public static final void start(final Command cmd) {
        new Thread() {
            @Override
            public void run() {
                blockStart(cmd);
            }
        }.start();
    }

    public static interface Command {
        /**
         * @param name
         * @param args
         * @return 返回true表示停止
         */
        boolean execute(String name, String... args) throws Exception;

        /**
         * @param th
         * @return 返回true表示停止
         */
        boolean onError(Throwable th);
    }
}
