package com.isnowfox.web.proxy;

import com.isnowfox.core.IocFactory;
import com.isnowfox.web.ActionObjectPool;
import com.isnowfox.web.Request;
import com.isnowfox.web.Response;
import com.isnowfox.web.ViewTypeInterface;
import com.isnowfox.web.config.ActionConfig;

public abstract class ActionProxy {
	private ActionConfig actionConfig;
	public abstract void invoke(IocFactory iocFactory, ActionObjectPool actionObjectPool, Request req, Response resp) throws Exception;
	ViewTypeInterface[] viewTypes;

	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	public ActionConfig getActionConfig() {
		return actionConfig;
	}
}
