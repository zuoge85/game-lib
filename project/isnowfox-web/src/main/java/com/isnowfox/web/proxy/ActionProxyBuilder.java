package com.isnowfox.web.proxy;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.isnowfox.core.IocFactory;
import com.isnowfox.util.ClassUtils;
import com.isnowfox.web.ActionObjectPool;
import com.isnowfox.web.Config;
import com.isnowfox.web.Request;
import com.isnowfox.web.Response;
import com.isnowfox.web.ViewTypeInterface;
import com.isnowfox.web.config.ActionConfig;
import com.isnowfox.web.config.ActionConfig.LiefCycleType;
import com.isnowfox.web.config.ParamsConfig;
import com.isnowfox.web.config.ViewConfig;

public class ActionProxyBuilder {
	private static final String ARG_IOC			=		"$1";
	private static final String ARG_POOL		=		"$2";
	private static final String ARG_REQ		=		"$3";
	private static final String ARG_RESP		=		"$4";
	
	
	private static final String PACKAGE_NAME = ActionProxyBuilder.class.getPackage().getName();
	
	private Class<? extends ActionProxy> cls;
	private ActionConfig actionConfig;
	private List<ViewTypeInterface> viewTypes = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public ActionProxyBuilder(ActionConfig actionConfig, Config config,
			ClassPool pool, int id) throws CannotCompileException, NotFoundException {
		this.actionConfig = actionConfig;
		
		CtClass cc = pool.makeClass(PACKAGE_NAME + ".ActionProxyImpi" + id); 
		
		cc.setSuperclass(pool.get(ActionProxy.class.getName()));
		cc.setModifiers(cc.getModifiers() | Modifier.FINAL);
		
		CtMethod method = CtNewMethod.make("public void invoke("+
							IocFactory.class.getCanonicalName()+" iocFactory, " +
							ActionObjectPool.class.getCanonicalName()+" actionObjectPool, "+
							Request.class.getCanonicalName() + " req, " +
							Response.class.getCanonicalName() + " resp" +
						") throws Exception;", cc);
		
		String body = makeMethodBody(actionConfig, config,pool);
		
		method.setBody(body);
		
		cc.addMethod(method);
		
		cls = cc.toClass();
		if(config.isDebug()){
			cc.debugWriteFile("debug");
		}
	}
	
	private String makeMethodBody(ActionConfig actionConfig, Config config,
			ClassPool pool) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		String className = actionConfig.getActionClass().getCanonicalName();
		if(config.isIocCreateObject()){
			sb.append(className + " actionObj = (" + className + ")" + ARG_IOC + ".getBean(" + className + ".class);\n");
		}else if(actionConfig.getLiefCycle() == LiefCycleType.REQUEST){
			sb.append(className + " actionObj =  (" + className + ")" + "new " + className + "();\n");
		}else if(actionConfig.getLiefCycle() == LiefCycleType.SINGLETON){
			sb.append(className + " actionObj = (" + className + ")" + ARG_POOL + ".get(" + className + ".class);\n");
		}else{
			throw new RuntimeException("未知的情况!,请检查是否修改了 生存周期类型");
		}
		
		
		sb.append("Object resultObj = actionObj." + actionConfig.getMethod() + "(");
		List<ParamsConfig.Item> list = actionConfig.getParamsConfig().getList();
		for (int i = 0; i < list.size(); i++) {
			ParamsConfig.Item item = list.get(i);
			if(i>0){
				sb.append(',');
			}
			String javaStr = StringEscapeUtils.escapeJava(item.getName() );
			switch (item.getType()) {
			case COOKIE:
				//getCookieString
				convert(sb, item.getCls(), ARG_REQ  + ".getCookieString(\"" + javaStr + "\")");
				break;
			case REQUEST:
				convert(sb, item.getCls(), ARG_REQ + ".getString(\"" + javaStr +"\")");
				break;
			case HEADER:
				convert(sb, item.getCls(), ARG_REQ + ".getHeader(\"" + javaStr +"\")");
				break;
			case IOC:
				convert(sb, item.getCls(), ARG_IOC +".getBean(\"" + javaStr +"\")");
				break;
			default:
				break;
			}
		}
		sb.append(");\n");
		
		//处理视图
		makeViewCode(sb, actionConfig, config, pool);
		
		//处理header这些
		sb.append("\n}\n");
		System.out.println(sb);
		return sb.toString();
	}
	
	private final void makeViewCode(StringBuilder sb, ActionConfig actionConfig, Config config,
			ClassPool pool){
		ViewConfig viewConfig = actionConfig.getViewConfig();
		switch (viewConfig.getMapType()) {
		case ONLY:
			viewTypes.add(viewConfig.getViewType());
			//doView(String pattern,Object action, Object result,String value, Request request, Response response) throws Exception;
			sb.append("viewTypes[0].doView(\"" + 
						StringEscapeUtils.escapeJava(actionConfig.getPattern()) +
						"\", actionObj , resultObj , \"" +
						StringEscapeUtils.escapeJava(viewConfig.getValue()) +
						"\" , " + ARG_REQ + "," + ARG_RESP +
					");\n");
			break;
		default:
			
			break;
		}
	}
	
	private final void convert(StringBuilder sb,Class<?> cls, String valueExpression){
		if(CharSequence.class.isAssignableFrom(cls)){
			sb.append(valueExpression);
		}else if(ClassUtils.isVoid(cls)){
			throw new RuntimeException("不支持Void类型参数!");
		}else{
			if(cls.isPrimitive()){
				sb.append(NumberUtils.class.getCanonicalName() + ".to" + convertFistUpper(cls.getName()) + "(" + valueExpression+ ")");
			}else if(ClassUtils.isWrap(cls)){
				Class<?> primitiveCls = ClassUtils.unwrap(cls);
				sb.append(cls.getCanonicalName() +"valueOf("+NumberUtils.class.getCanonicalName() + ".to" + convertFistUpper(primitiveCls.getName()) + "(" + valueExpression+ "))");
			}else{
				throw new RuntimeException("暂时不支持的类型:" + cls);
			}
		}
	}
	private final String convertFistUpper(String name){
		return Character.toUpperCase(name.charAt(0)) + name.substring(1, name.length());
	}
	public ActionProxy build() throws InstantiationException, IllegalAccessException {
		ActionProxy obj = cls.newInstance();
		obj.viewTypes = viewTypes.toArray(new ViewTypeInterface[viewTypes.size()]);
		return obj;
	}
}
