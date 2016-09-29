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

public class DaoImplCreate {
    private static final Logger log = LoggerFactory.getLogger(DaoImplCreate.class);

    public static void create(List<Table> tl, Config config, String objectPack, String daoPack, String daoImplPack) throws Exception {
        File dir;
        if (daoImplPack == null) {
            dir = config.getPackPath(daoPack);
        } else {
            dir = config.getPackPath(daoPack + "." + daoImplPack);
        }
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("创建路径失败:" + dir.getAbsolutePath());
            }
        }
//        Engine engine = HttlUtils.getEngine();
        for (Table ta : tl) {
            File f = new File(dir, ta.getClassName() + "Dao.java");
            log.debug("ObjectCreate file:{}", f.getAbsolutePath());
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
                Map<String, Object> context = new HashMap<>();
                context.put("t", ta);
                context.put("beanPack", config.getPack(objectPack));

                String pack = config.getPack(daoPack);
                if (daoImplPack == null) {
                    context.put("implPack", pack);
                } else {
                    context.put("implPack", config.getPack(pack, daoImplPack));
                }

                context.put("daoPack", pack);
                context.put("sql", new SqlUtils());

                HttlUtils.render("/org/forkjoin/jdbckit/mysql/template/daoImpl.httl", context, out);
            }
//			VelocityContext context = new VelocityContext(); 
//			context.put("t", ta);
//			context.put("beanPack", config.getPack(objectPack));
//			
//			String pack=config.getPack(daoPack);
//			context.put("daoPack", pack);
//			context.put("sql", new SqlUtils());
//			if(daoImplPack == null){
//				context.put("implPack", pack);
//			}else{
//				context.put("implPack", config.getPack(pack,daoImplPack));
//			}
//			//Template t = ve.getTemplate("xml/object.vm","UTF-8");
////			Template t = ve.getTemplate("","UTF-8");
//			//Writer w=new OutputStreamWriter(System.out);
//			File f=new File(dir,ta.getClassName()+"Dao.java");
//			log.debug("DaoImplCreate file:{}",f.getAbsolutePath());
//			Writer fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf8"));
//			Reader read=new InputStreamReader(ObjectCreate.class.getResourceAsStream("template/daoImpl.vm"),"utf8");
//			Velocity.evaluate(context, fw, "temp",read);
//			//t.merge(context,w);
//			//t.merge(context, fw);
//			//w.flush();
//			fw.close();
        }
    }
}
