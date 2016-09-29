package com.isnowfox.service;

import com.isnowfox.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author zuoge85 on 2014/12/14.
 */
public final class ServiceMethodInfo {
    private String name;
    private String impiName;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;

    public ServiceMethodInfo(String name, String impiName, Class<?>[] parameterTypes, Class<?> returnType) {
        this.name = name;
        this.impiName = impiName;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    public StringBuilder toParameterString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("final ");
            Class<?> parameterType = parameterTypes[i];
            sb.append(ClassUtils.getClassName(parameterType));
            sb.append(" ");
            sb.append(ClassBuilder.ARG_NAME);
            sb.append(i);
        }
        return sb;
    }


    public StringBuilder toParameterNameString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(" ");
            sb.append(ClassBuilder.ARG_NAME);
            sb.append(i);
        }
        return sb;
    }

    public void checkImpi(Method method) throws ServiceException {
        if (!Arrays.equals(method.getParameterTypes(), parameterTypes)) {
            throw new ServiceException(this + "接口服务方法和实现方法参数不一致["
                    + Arrays.toString(method.getParameterTypes()) + ":" + Arrays.toString(parameterTypes) + "]");
        }

        if (!returnType.equals(method.getReturnType())) {
            throw new ServiceException(this + "接口服务方法和实现方法返回值不一致["
                    + method.getReturnType() + ":" + returnType + "]");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImpiName() {
        return impiName;
    }

    public void setImpiName(String impiName) {
        this.impiName = impiName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "ServiceMethodInfo{" +
                "name='" + name + '\'' +
                ", impiName='" + impiName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", returnType=" + returnType +
                '}';
    }

}
