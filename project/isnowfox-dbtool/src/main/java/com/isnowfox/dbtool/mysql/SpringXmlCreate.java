package com.isnowfox.dbtool.mysql;

import httl.Engine;
import httl.Template;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringXmlCreate {
	private static final Logger log = LoggerFactory.getLogger(SpringXmlCreate.class);
	public static void create(List<Table> tl,Config config,String objectPack,String daoPack,String daoImplPack) throws ResourceNotFoundException, ParseErrorException, Exception{
		File dir=config.getResourcesDir();
		if(!dir.exists()){
			dir.mkdir();
		}
		Engine engine = Engine.getEngine();
		
		File f=new File(dir,config.getContextFileName());
		log.debug("ObjectCreate file:{}",f.getAbsolutePath());
		try(OutputStream out = new BufferedOutputStream(new FileOutputStream(f))){
			Map<String, Object> context = new HashMap<String, Object>();
			String pack=config.getPack(daoPack);
			context.put("pack", pack);
			context.put("tables",tl);
			
			Template template = engine.getTemplate("/com/isnowfox/dbtool/mysql/template/springContextImpl.httl");
			template.render(context, out);
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
