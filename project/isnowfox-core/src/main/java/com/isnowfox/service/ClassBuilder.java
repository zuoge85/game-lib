package com.isnowfox.service;

import com.isnowfox.compiler.StringCompilerUtils;
import com.isnowfox.compiler.StringObject;
import com.isnowfox.service.annotation.ServiceMethod;
import com.isnowfox.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import javax.tools.*;
import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成函数对象
 *
 * @author zuoge85
 */
class  ClassBuilder<T> {
    private static final String PACKAGE_NAME = ServiceEngine.class.getPackage().getName();
//    public static final String IMPI_FIELD_NAME = "$impi";
    public static final String ARG_NAME = "arg";

    private static final AtomicInteger idSpeed = new AtomicInteger();
    private HashMap<String, ServiceMethodInfo> methodInfoMap;

    private Class<?> interfaceCls;
    private Class<?> impiCls;
    private Class<?> cls;
    private String className;

    ClassBuilder(ServiceEngine serviceEngine, Class<T> interfaceCls, Class<?> impiCls) throws ServiceException {
        this.interfaceCls = interfaceCls;
        this.impiCls = impiCls;

        className = "_ServiceClass" + idSpeed.getAndIncrement();
        methodInfoMap = analyseImpiCls();
        try {
            Constructor constructor = impiCls.getDeclaredConstructor();
            if (!(Modifier.isPublic(constructor.getModifiers()) || Modifier.isProtected(constructor.getModifiers()))) {
                throw new ServiceException("实现类[" + impiCls + "]空构造函数 不能被子类访问，必须public 或者 protected");
            }
        } catch (Exception e) {
            throw new ServiceException("实现类[" + impiCls + "]没有空构造函数", e);
        }
        check();
        makeClass();
    }

    @SuppressWarnings("unchecked")
    public  T builder() throws ServiceException {
        try {
            return (T)cls.newInstance();
        } catch (Exception e) {
            throw new ServiceException("实现类[" + cls + "]构造失败", e);
        }
    }

    private  void makeClass() throws ServiceException {
        try {
            StringBuilder javaCode = makeClassCode();
            cls = StringCompilerUtils.compiler(PACKAGE_NAME +"." + className, javaCode);
        } catch (Exception ex) {
            throw new ServiceException("生成代理类失败！", ex);
        }
    }


    private StringBuilder makeClassCode() {
        StringBuilder sb = new StringBuilder();
        Collection<ServiceMethodInfo> values = methodInfoMap.values();

        sb.append("package ").append(PACKAGE_NAME).append(";\n")
                .append("\n")
                .append("public final class ").append(className).append(" extends ").append(ClassUtils.getClassName(BaseService.class)).append(" implements ").append(ClassUtils.getClassName(interfaceCls)).append(" {\n")
                .append("    private InnerImpi _impi = new InnerImpi();\n")
                .append("\n");

        for (ServiceMethodInfo info : values) {
            sb.append("    \n")
                    .append("    public void ").append(info.getName()).append("(").append(info.toParameterString()).append(") {\n")
                    .append("        ").append(info.getName()).append("_run").append(" o = new ").append(info.getName()).append("_run();\n");

            Class<?>[] parameterTypes = info.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
//                Class<?> aClass = parameterTypes[i];
                sb.append("        o.arg").append(i).append(" = arg").append(i).append(";\n");
            }
            sb.append("        execute(o);\n")
                    .append("    }\n");
        }


        sb.append("\n")

                .append("    public static final class InnerImpi extends ").append(ClassUtils.getClassName(impiCls)).append("{\n");

        for (ServiceMethodInfo info : values) {
            sb.append("        @Override\n")
                    .append("        public void ").append(info.getImpiName()).append(" (").append(info.toParameterString()).append(") {\n")
                    .append("            super.").append(info.getImpiName()).append("(").append(info.toParameterNameString()).append(");\n")
                    .append("        }\n");
        }

        sb.append("    }\n")
                .append("\n");


        for (ServiceMethodInfo info : values) {
            sb.append("    public  class ").append(info.getName()).append("_run implements Runnable{\n");

            Class<?>[] parameterTypes = info.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> aClass = parameterTypes[i];
                sb.append("        private ").append(ClassUtils.getClassName(aClass)).append(" ").append(ARG_NAME).append(i).append(";\n");
            }


            sb.append("        @Override\n")
                    .append("        public void run() {\n")
                    .append("            _impi.").append(info.getImpiName()).append("(").append(info.toParameterNameString()).append(");\n")
                    .append("        }\n")
                    .append("    }\n");
        }


        sb.append("}\n");
        return sb;
    }


    private HashMap<String, ServiceMethodInfo> analyseImpiCls() throws ServiceException {
        HashMap<String, ServiceMethodInfo> methodInfoMap = new HashMap<>();
        Method[] methods = impiCls.getDeclaredMethods();
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                ServiceMethod annotation = method.getAnnotation(ServiceMethod.class);
                if (annotation != null) {
                    int modifiers = method.getModifiers();
                    if (!Modifier.isProtected(modifiers)) {
                        throw new ServiceException("服务方法[" + method + "]必须是 protected的");
                    }
                    String name = annotation.value();
                    if (StringUtils.isEmpty(name)) {
                        name = method.getName();
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> returnType = method.getReturnType();
                    if (!Void.TYPE.equals(returnType)) {
                        throw new ServiceException("服务方法[" + method + "]返回值必须是空");
                    }
                    ServiceMethodInfo info = new ServiceMethodInfo(name, method.getName(), parameterTypes, returnType);
                    methodInfoMap.put(name, info);
                }
            }
        }
        return methodInfoMap;
    }

    private void check() throws ServiceException {
        Method[] methods = interfaceCls.getDeclaredMethods();
        @SuppressWarnings("unchecked")
        HashMap<String, ServiceMethodInfo> cloneMap = (HashMap<String, ServiceMethodInfo>) methodInfoMap.clone();
        if (methods != null) {
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                ServiceMethodInfo info = methodInfoMap.get(method.getName());
                if (info == null) {
                    throw new ServiceException("接口方法[" + method.getName() + "]未在实现类定义");
                }
                info.checkImpi(method);
                cloneMap.remove(method.getName());
            }
        }
        if (!cloneMap.isEmpty()) {
            throw new ServiceException("服务方法 未在接口定义[" + cloneMap + "]未在接口定义");
        }
    }


}
