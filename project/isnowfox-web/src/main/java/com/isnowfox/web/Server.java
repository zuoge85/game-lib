package com.isnowfox.web;

import com.isnowfox.core.IocFactory;
import com.isnowfox.web.adapter.WebPipelineFactory;
import com.isnowfox.web.config.ActionConfig;
import com.isnowfox.web.impl.DispatcherFactory;
import com.isnowfox.web.listener.WebListener;
import com.isnowfox.web.listener.WebRequestEvent;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;


public class Server {
    private static final Runtime runtime = Runtime.getRuntime();
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static Server create(Config config) {
        return new Server(config);
    }

    private List<ActionConfig> actionList = new ArrayList<>();
    private List<ViewTypeInterface> viewTypes = new ArrayList<>();
    private IocFactory iocFactory;
    private Config config;
    private Dispatcher dispatcher;
    private List<WebListener> listeners = new ArrayList<>();
    private ServerBootstrap webBootstrap;

    public Server(Config config) {
        this.config = config;
    }

    ;

    void initConfig() {

    }

    /**
     * 启动服务器
     *
     * @throws Exception
     */
    public void start() throws Exception {
//		defaultViewHandler=regViewHandler("httl",new HttlViewHandler());
//		regViewHandler("json",new JsonViewHandler());
        //invoke=new CglibInvoke();
        for (ViewType viewType : ViewType.values()) {
            regViewType(viewType);
        }

        dispatcher = DispatcherFactory.create(config, this);
        long freeMem = runtime.freeMemory();
        log.info(
                "开始启动:[JVM freeMem:{}MB ; JVM maxMemory:{}MB ; JVM totalMemory:{}MB ]",
                freeMem / 1024f / 1024f, runtime.maxMemory() / 1024f / 1024f,
                runtime.totalMemory() / 1024f / 1024f);
        try {
            webBootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                            Executors.newCachedThreadPool(), Executors.newCachedThreadPool()
                    )
            );
            webBootstrap.setPipelineFactory(new WebPipelineFactory(this, config));
            int[] ports = config.getPorts();
            for (int i = 0; i < ports.length; i++) {
                webBootstrap.bind(new InetSocketAddress(ports[i]));
            }

            log.info("服务器启动成功:[JVM freeMem:{}MB ; JVM maxMemory:{}MB ; JVM totalMemory:{}MB ]",
                    freeMem / 1024f / 1024f, runtime.maxMemory() / 1024f / 1024f, runtime.totalMemory() / 1024f / 1024f);
            System.gc();
        } catch (Exception e) {
            log.error("严重错误!服务器启动失败", e);
        }
    }

    public void close() {
        webBootstrap.shutdown();
    }

    public ActionConfig regSingleton(Class<?> cls, String methodAndPattern) {
        return regSingleton(cls, methodAndPattern, methodAndPattern);
    }

    public ActionConfig regSingleton(Class<?> cls, String method, String pattern) {
        ActionConfig action = new ActionConfig(cls, method, pattern, config, ActionConfig.LiefCycleType.SINGLETON);
        actionList.add(action);
        return action;
    }

    public ActionConfig regRequest(Class<?> cls, String methodAndPattern) {
        return regRequest(cls, methodAndPattern, methodAndPattern);
    }

    public ActionConfig regRequest(Class<?> cls, String method, String pattern) {
        ActionConfig action = new ActionConfig(cls, method, pattern, config, ActionConfig.LiefCycleType.REQUEST);
        actionList.add(action);
        return action;
    }

    public ViewTypeInterface regViewType(ViewTypeInterface viewType) throws Exception {
        viewType.init(config);
        viewTypes.add(viewType);
        return viewType;
    }

    public void addListener(WebListener listener) {
        listeners.add(listener);
    }

    public WebRequestEvent fireRequestStartEvent(Request req) {
        WebRequestEvent evt = new WebRequestEvent(req);
        if (!listeners.isEmpty()) {
            for (WebListener l : listeners) {
                l.requestHandlerStart(evt);
            }
        }
        return evt;
    }

    public void fireRequestEndEvent(WebRequestEvent evt) {
        if (!listeners.isEmpty()) {
            for (WebListener l : listeners) {
                l.requestHandlerEnd(evt);
            }
        }
    }

    public IocFactory getIocFactory() {
        return iocFactory;
    }

    public void setIocFactory(IocFactory iocFactory) {
        this.iocFactory = iocFactory;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public List<ActionConfig> getActionList() {
        return Collections.unmodifiableList(actionList);
    }
}
