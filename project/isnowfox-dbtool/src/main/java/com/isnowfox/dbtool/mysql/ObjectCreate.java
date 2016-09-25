package com.isnowfox.dbtool.mysql;

import httl.Engine;
import httl.Template;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectCreate {
	private static final Logger log = LoggerFactory.getLogger(ObjectCreate.class);
	public static void create(List<Table> tl,Config config,String childPack) throws ResourceNotFoundException, ParseErrorException, Exception{
		File dir=config.getPackPath(childPack);
		if(!dir.exists()){
			dir.mkdirs();
		}
		Engine engine = Engine.getEngine();
		for(Table ta:tl){
			File f=new File(dir,ta.getClassName()+"DO.java");
			log.debug("ObjectCreate file:{}",f.getAbsolutePath());
			try(OutputStream out = new BufferedOutputStream(new FileOutputStream(f))){
				Map<String, Object> context = new HashMap<String, Object>();
				context.put("t", ta);
				context.put("sql", new SqlUtils());
				context.put("pack", config.getPack(childPack));
				
				Template template = engine.getTemplate("/com/isnowfox/dbtool/mysql/template/object.httl");
				template.render(context, out);
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
