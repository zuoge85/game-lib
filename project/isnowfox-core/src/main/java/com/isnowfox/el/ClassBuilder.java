package com.isnowfox.el;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.isnowfox.el.exp.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import com.isnowfox.el.KeyInfo.Type;
import com.isnowfox.util.ArrayExpandUtils;

/**
 * 生成函数对象
 * @author zuoge85
 *
 */
class ClassBuilder  {
//	private static final String EXP_VAR_NAME = "exp";

	private static final String PACKAGE_NAME = ElEngine.class.getPackage().getName();
	
	private Class<?> returnClass;
	private CtMethod method;
	private Class<?> cls;
	//private OelEngine oel;
	
	//Expression<?> exp;
	//private boolean isHasExp;
	
	ClassBuilder(ElEngine oel, ClassPool pool, Class<?> cls, String el,int id) throws UnknownKeyException, CannotCompileException, NotFoundException{
	//	this.oel = oel;
		CtClass cc = pool.makeClass(PACKAGE_NAME + ".OelProxyImpi" + id); 
		cc.setModifiers(cc.getModifiers()|Modifier.FINAL);
		String body = makeMethodBody(cls, el);
		//CtField cf = new CtField(pool.get(Expression.class.getCanonicalName()), EXP_VAR_NAME, cc);
		//cc.addField(cf, OelEngine.class.getCanonicalName() + ".getInstance().compile(var_" + varSeed + ", \"sb\")");
		if(returnClass.isPrimitive()){
			cc.setSuperclass(pool.get(ExpressionInner.map.get(returnClass).getName()));
			method = CtNewMethod.make("public " + returnClass.getCanonicalName() + " "+returnClass.getCanonicalName()+"El(Object obj);", cc);
			method.setBody(body);
		}else{
			cc.addInterface(pool.get(Expression.class.getCanonicalName()));
			method = CtNewMethod.make("public Object el(Object obj);", cc);
			method.setBody(body);
		}
		cc.addMethod(method);
		if(ElEngine.DEBUG){
			cc.debugWriteFile("debug");
		}
		this.cls = cc.toClass();
		cc.detach();
	}
	
	private final String makeMethodBody(Class<?> cls, String el) throws UnknownKeyException{
		int varSeed = 0;
		StringBuilder sb = new StringBuilder("{\n");
		String[] keys = el.split("\\.");
		sb.append(cls.getCanonicalName() + " var_" + (++varSeed) + " = (" +cls.getCanonicalName() + ")$1;\n" );
		returnClass = cls;
		for (int i = 0; i < keys.length; i++) {
			String key =  keys[i].trim();
			KeyInfo info =analyse(returnClass, key);
			if(info == null){
				//处理引用类型不能处理的情况
				//
				//exp = oel
				if(i==0){
					throw new UnknownKeyException("位置:" + i + ",未知的key:"+key +",cls:"+ cls);
				}else{
					int nextVar = varSeed++ ;
					String itemEl = ArrayExpandUtils.join(keys, '.', i);
					sb.append("Object  " +"var_" + varSeed + " = " + ElEngine.class.getCanonicalName() + ".getInstance().el(var_" + nextVar + ", \"" +itemEl+ "\");\n");
				}
			}else{
				int nextVar = varSeed++ ;
				switch (info.getType()) {
				case FIELD:
					sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + key + ";\n");
					break;
				case PROPERTY:
					sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + info.getMethodName() + "();\n");
					break;
				case METHOD:
					sb.append(info.getCls().getCanonicalName() +" var_" + varSeed + " = var_"+ nextVar +"." + key + ";\n");
					break;
				default:
					break;
				}
				returnClass = info.getCls();
			}
		}
		if(returnClass.isPrimitive()){
			sb.append("return var_" + varSeed + ";\n}");
		}else{
			sb.append("return var_" + varSeed + ";\n}");
		}
		return sb.toString();
	}

	private final KeyInfo analyse(Class<?> cls,String key) throws UnknownKeyException{
		try {
			if(key.endsWith("()")){
				try {
					Method method = cls.getMethod(key.substring(0, key.length()-2));
					return new KeyInfo(Type.METHOD, method.getReturnType());
				} catch (Exception e2) {
					
				}
			}
			PropertyDescriptor prop = new PropertyDescriptor(key, cls);
			return new KeyInfo(Type.PROPERTY, prop.getPropertyType(), prop.getReadMethod().getName());
		} catch (IntrospectionException e) {
			//属性没找到那么可能是field
			Field field;
			try {
				field = cls.getField(key);
				return new KeyInfo(Type.FIELD, field.getType());
			} catch (Exception e1) {
				
			}
		}
		return null;
		//throw new UnknownKeyException("未知的 key:"+key +",cls:"+ cls);
	}
	
	public Object build() throws InstantiationException, IllegalAccessException {
		return cls.newInstance();
	}
	
	private static class  ExpressionInner{
		private static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>(16);
		static{
			map.put(boolean.class, BooleanExpression.class);
			map.put(byte.class, ByteExpression.class);
			map.put(char.class, CharExpression.class);
			map.put(double.class, DoubleExpression.class);
			map.put(float.class, FloatExpression.class);
			map.put(int.class, IntExpression.class);
			map.put(long.class, LongExpression.class);
			map.put(short.class, ShortExpression.class);
			map.put( void.class, VoidExpression.class);
		}
	}
}
