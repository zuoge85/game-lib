package com.isnowfox.web.proxy;

import com.isnowfox.web.Config;
import com.isnowfox.web.config.ActionConfig;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zuoge85
 */
public class ProxyManager {
    //	private static final ProxyManager instance = new ProxyManager();
//	public static final ProxyManager getInstance(){
//		return instance;
//	}
    public ProxyManager() {

    }

    private Map<String, ActionProxy> map = new HashMap<>();

    public ActionProxy getActionProxy(String pattern) {
        return map.get(pattern);
    }

    public void initAction(List<ActionConfig> list, Config config) throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException {
        //初始化 action代理类
        ClassPool pool = ClassPool.getDefault();
        for (int i = 0; i < list.size(); i++) {
            ActionConfig actionConfig = list.get(i);
            initAction(pool, actionConfig, config, i);
        }
    }

    private void initAction(ClassPool pool, ActionConfig actionConfig, Config config, int i) throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException {
        ActionProxyBuilder build = new ActionProxyBuilder(actionConfig, config, pool, i);
        ActionProxy actionProxy = build.build();
        actionProxy.setActionConfig(actionConfig);
        map.put(actionConfig.getPattern(), actionProxy);
    }

    public ActionProxy get(String pattern) {
        return map.get(pattern);
    }
}
