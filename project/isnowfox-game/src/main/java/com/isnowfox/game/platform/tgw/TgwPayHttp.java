package com.isnowfox.game.platform.tgw;

import com.isnowfox.core.IocFactory;
import com.isnowfox.util.ArrayExpandUtils;
import com.isnowfox.web.Config;
import com.isnowfox.web.ParameterType;
import com.isnowfox.web.Server;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zuoge85 on 14-4-3.
 */
public class TgwPayHttp {
    private static final Logger log = LoggerFactory.getLogger(TgwPayHttp.class);

    public static Server start(IocFactory ioc) {
        Config cfg = new Config();
//        SpringIocFactory ioc = new SpringIocFactory("CmsContext.xml");
        //LSConfig lsconfig = ioc.getBean(LSConfig.class);
        cfg.setPorts(9001, 8000);
        cfg.setDebug(true);
        cfg.setSuffix("do");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        cfg.setTemplateFilePath("template");
        cfg.setStaticFilePath(null);
        cfg.setEnableHttl(false);
        //启动rmi服务器
        //启动http服务器
        log.info("启动http服务器");
        Server server = new Server(cfg);
        try {
            server.setIocFactory(ioc);

            server.regSingleton(PayAction.class, "pay", "pay")
                    .json();
            server.regSingleton(PayAction.class, "error","error")
                    .json().header("Access-Control-Allow-Origin", "*");

            server.regSingleton(PayAction.class, "gamelogin","gamelogin")
                    .json().header("Access-Control-Allow-Origin", "*");

            server.start();
            return server;
        } catch (Exception e) {
            log.error("严重错误,启动失败!", e);
        }
        return server;
    }
}
