package com.isnowfox.dbtool.mysql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoCreate {
	private static final Logger log = LoggerFactory.getLogger(DaoCreate.class);
	public static void create(List<Table> tl,Config config,String objectPack,String daoPack) throws ResourceNotFoundException, ParseErrorException, Exception{
		File dir=config.getPackPath(daoPack);
		if(!dir.exists()){
			dir.mkdir();
		}
		for(Table ta:tl){
			VelocityContext context = new VelocityContext(); 
			context.put("t", ta);
			context.put("pack", config.getPack(daoPack));
			context.put("beanPack", config.getPack(objectPack));
			//Template t = ve.getTemplate("xml/object.vm","UTF-8");
//			Template t = ve.getTemplate("","UTF-8");
			//Writer w=new OutputStreamWriter(System.out);
			File f=new File(dir,ta.getClassName()+"Dao.java");
			log.debug("DaoCreate file:{}",f.getAbsolutePath());
			Writer fw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"utf8"));
			Reader read=new InputStreamReader(ObjectCreate.class.getResourceAsStream("xml/dao.vm"),"utf8");
			Velocity.evaluate(context, fw, "temp",read);
			//t.merge(context,w);
			//t.merge(context, fw);
			//w.flush();
			fw.close();
		}
	}
}
