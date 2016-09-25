package com.isnowfox.web;

import com.google.common.collect.Maps;
import com.isnowfox.core.JsonTransform;
import com.isnowfox.util.JsonUtils;
import httl.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;


public enum ViewType implements ViewTypeInterface {

    //	public static final String JSON = "JSON";
//	public static final String STREAM = "STREAM";
//	public static final String HTTL = "HTTL";
    JSON {
        @Override
        public void doView(String pattern, Object action, Object result, String value, Request request, Response response) throws Exception {
            if (result == null) {

            } else if (result instanceof CharSequence) {
                String str = result.toString();
                response.oneWrite(str.getBytes(request.getCharset()));
            } else if (result instanceof JsonTransform) {
                JsonTransform jt = (JsonTransform) result;
                jt.toJson(response.getAppendable());
            } else {
                JsonUtils.mapper.writeValue(response.getWriter(), result);
            }
        }
    },
    STREAM,
    HTTL {
        private String extName;
        private Engine engine;

        @Override
        public void init(Config config) throws Exception {
            if (config.isEnableHttl()) {
                Properties configure = new Properties();
                URL url = ViewType.class.getResource("/httl.properties");
                log.info("配置路径:{}", url);
                InputStream is = url.openStream();
                try {
                    configure.load(is);
                    log.info("启动httl模板引擎:[template.directory:{}]", config.getTemplateFilePath());
                    configure.put("template.directory", config.getTemplateFilePath());
                    engine = Engine.getEngine(configure);
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }

                extName = "." + config.getHttlTemplateFileSuffix();
            }
        }

        @Override
        public void doView(String pattern, Object action, Object result, String value, Request request, Response response) throws Exception {
            if (engine == null) {
                throw new Exception("为开起httl支持！");
            }
            Map<String, Object> context = Maps.newHashMap();
            context.put("context", Context.getInstance().getAttributesMap());
            String viewName = null;
            if (result instanceof View) {
                View v = (View) result;
                viewName = v.getName();
                result = v.getValue();
            } else if (result instanceof CharSequence) {
                viewName = String.valueOf(result);
            } else {

            }
            context.put("result", result);
            context.put("r", result);
            if (viewName == null) {
                viewName = pattern;
            }
            if (viewName != null) {
                engine.getTemplate(viewName + extName).render(context, response.getWriter());
            }
        }
    };

    private final static Logger log = LoggerFactory.getLogger(ViewType.class);

    @Override
    public void doView(String pattern, Object action, Object result, String value, Request request,
                       Response response) throws Exception {
    }

    @Override
    public void init(Config config) throws Exception {

    }
}
