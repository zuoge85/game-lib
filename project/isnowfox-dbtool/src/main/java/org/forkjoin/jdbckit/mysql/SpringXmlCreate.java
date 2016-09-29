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

public class SpringXmlCreate {
    private static final Logger log = LoggerFactory.getLogger(SpringXmlCreate.class);

    public enum Type {
        BASE("DaoContext.xml", "", ""),
        READ_ONLY("ReadOnlyDaoContext.xml", ".readonly", "ReadOnly"),
        CACHE("CacheDaoContext.xml", ".cache", "Cache");

        final String contextFileName;
        final String packName;
        final String anotherName;

        Type(String contextFileName, String packName, String anotherName) {
            this.contextFileName = contextFileName;
            this.packName = packName;
            this.anotherName = anotherName;
        }
    }


    public static void create(Type type, List<Table> tl, Config config, String objectPack, String daoPack, String daoImplPack) throws Exception {
        File dir = config.getResourcesPackPath("");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("创建路径失败:" + dir.getAbsolutePath());
            }
        }


        File f = new File(dir, type.contextFileName);
        log.debug("ObjectCreate file:{}", f.getAbsolutePath());
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(f))) {
            Map<String, Object> context = new HashMap<String, Object>();
            String pack = config.getPack(daoPack);
            context.put("pack", pack + type.packName);
            context.put("tables", tl);
            context.put("anotherName", type.anotherName);

            HttlUtils.render("/org/forkjoin/jdbckit/mysql/template/springContextImpl.httl", context, out);
        }

//		VelocityContext context = new VelocityContext(); 
//		String pack=config.getPack(daoPack);
//		
//		
//		log.debug("SpringXmlCreate file:{}",f.getAbsolutePath());
//		Writer fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf8"));
//		Reader read=new InputStreamReader(ObjectCreate.class.getResourceAsStream("template/springContextImpl.vm"),"utf8");
//		Velocity.evaluate(context, fw, "temp",read);
//		//t.merge(context,w);
//		//t.merge(context, fw);
//		//w.flush();
//		fw.close();
//		for(Table ta:tl){
//			VelocityContext context = new VelocityContext(); 
//			context.put("t", ta);
//			context.put("beanPack", config.getPack(objectPack));
//			
//			String pack=config.getPack(daoPack);
//			context.put("daoPack", pack);
//			context.put("sql", new SqlUtils());
//			context.put("implPack", config.getPack(pack,daoImplPack));
//			//Template t = ve.getTemplate("xml/object.vm","UTF-8");
////			Template t = ve.getTemplate("","UTF-8");
//			//Writer w=new OutputStreamWriter(System.out);
//			File f=new File(dir,ta.getClassName()+"DaoImpl.java");
//			log.debug("DaoCreate file:{}",f.getAbsolutePath());
//			Writer fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf8"));
//			Reader read=new InputStreamReader(ObjectCreate.class.getResourceAsStream("xml/daoImpl.vm"),"utf8");
//			Velocity.evaluate(context, fw, "temp",read);
//			//t.merge(context,w);
//			//t.merge(context, fw);
//			//w.flush();
//			fw.close();
//		}
    }
}
