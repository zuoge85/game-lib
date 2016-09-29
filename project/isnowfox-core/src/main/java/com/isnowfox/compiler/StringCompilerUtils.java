package com.isnowfox.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

/**
 * @author zuoge85 on 2014/12/15.
 */
public class StringCompilerUtils {

//    private static final List<String> options = compilerOption();

    public static URI url(String name, JavaFileObject.Kind kind) {
        return URI.create("string:///" + name.replace('.', '/') + kind.extension);
    }

    private static JavaCompiler compiler;
    private static BytesFileManager fileManager;

    static {
        compiler = ToolProvider.getSystemJavaCompiler();
        fileManager = new BytesFileManager(compiler.getStandardFileManager(null, null, null));
        if (compiler == null) {
            throw new RuntimeException("需要编译api，classpath里面没用tools.jar");
        }
    }

    public static synchronized Class<?> compiler(String className, StringBuilder javaCode) throws Exception {
        StringObject so = new StringObject(className, javaCode);
        StringWriter stringWriter = new StringWriter();

        JavaCompiler.CompilationTask task = compiler.getTask(stringWriter, fileManager, null, null, null, Arrays.asList(so));
        Boolean result = task.call();
        if (!result) {
            throw new RuntimeException("编译失败！\n" + stringWriter);
        }
        return fileManager.getClassLoader(null).loadClass(className);
    }


//    private static List<String> compilerOption() {
//        List<String> options = new ArrayList<>(Arrays.asList("-d", "d:", "-encoding", "utf8"));
//        URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
//        URL[] urLs = cl.getURLs();
//        if (urLs != null && urLs.length > 0) {
//            options.add("-classpath");
//            StringBuilder sb = new StringBuilder();
//            for (URL url : urLs) {
//                if (sb.length() > 0) {
//                    sb.append(File.pathSeparatorChar);
//                }
//                sb.append(url.getPath());
//            }
//            options.add(sb.toString());
//        }
//        return options;
//    }
}
