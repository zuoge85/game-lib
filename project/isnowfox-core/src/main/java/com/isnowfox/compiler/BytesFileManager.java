package com.isnowfox.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zuoge85 on 2014/12/15.
 */
public class BytesFileManager extends
        ForwardingJavaFileManager {

    /**
     * 存储编译后的字节码
     */
    private BytesJavaFileObject jclassObject;
    private Map<String, BytesJavaFileObject> map = new HashMap<>();

    public BytesFileManager(StandardJavaFileManager standardManager) {
        super(standardManager);
    }

    /**
     * 调用这个获取类加载器,进而加载动态编译后的字节码类
     */
    @Override
    public ClassLoader getClassLoader(Location location) {
        return secureClassLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            Location location, String className,
            JavaFileObject.Kind kind,
            FileObject sibling) throws IOException {
        BytesJavaFileObject fileObject = new BytesJavaFileObject(className, kind);
        map.put(className, fileObject);
        return fileObject;
    }

    private SecureClassLoader secureClassLoader = new SecureClassLoader(ClassLoader.getSystemClassLoader()) {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            BytesJavaFileObject fileObject = map.get(name);
            if (fileObject != null) {
                byte[] b = fileObject.getBytes();
                map.remove(name);
                return super.defineClass(name, b, 0, b.length);
            }
            return null;
        }
    };
}