package org.forkjoin.jdbckit.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectCreate {
    private static final Logger log = LoggerFactory.getLogger(ObjectCreate.class);

    public static void create(List<Table> tl, Config config, String childPack) throws Exception {
        File dir = config.getPackPath(childPack);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("创建路径失败:" + dir.getAbsolutePath());
            }
        }


        for (Table ta : tl) {
            File f = new File(dir, ta.getClassName() + "DO.java");
            log.debug("ObjectCreate file:{}", f.getAbsolutePath());
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
                Map<String, Object> context = new HashMap<>();
                context.put("t", ta);
                context.put("sql", new SqlUtils());
                context.put("pack", config.getPack(childPack));

                HttlUtils.render(
                        "/org/forkjoin/jdbckit/mysql/template/object.httl",
                        context, out
                );
            }

//			//VelocityContext context = new VelocityContext(); 
//			context.put("t", ta);
//			context.put("sql", new SqlUtils());
//			context.put("pack", config.getPack(childPack));
//			//Template t = ve.getTemplate("xml/object.vm","UTF-8");
////			Template t = ve.getTemplate("","UTF-8");
//			//Writer w=new OutputStreamWriter(System.out);
//			
//			Writer fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf8"));
//			Reader read=new InputStreamReader(ObjectCreate.class.getResourceAsStream("template/object.vm"),"utf8");
//			Velocity.evaluate(context, fw, "temp",read);
            //t.merge(context,w);
            //t.merge(context, fw);
            //w.flush();
//			fw.close();
        }
    }
}
