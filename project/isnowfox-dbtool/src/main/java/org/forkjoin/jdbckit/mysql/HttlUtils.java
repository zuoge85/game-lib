package org.forkjoin.jdbckit.mysql;

import httl.Engine;
import httl.Template;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;

/**
 * @author zuoge85 on 15/7/13.
 */
public class HttlUtils {

    private static Engine getEngine() {
        return Inner.engine;
    }

    private static class Inner {
        static final Engine engine;

        static {
            Properties configProperties = new Properties();
//            configProperties.load(ObjectCreate.class.getResourceAsStream("/com/isnowfox/dbtool/mysql/httl.properties"));
            try {
                configProperties.load(HttlUtils.class.getResourceAsStream("/org/forkjoin/jdbckit/mysql/httl.properties"));
            } catch (IOException e) {
                throw new RuntimeException();
            }
            engine = Engine.getEngine(configProperties);
        }
    }

    public static void render(String name, Map<String, Object> context, OutputStream out) throws IOException, ParseException {
        Engine engine = getEngine();
        Template template = engine.getTemplate(name);
        template.render(context, out);
    }
}
