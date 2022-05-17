package com.gencode.template;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;

public class MyGenConfig extends GenConfig{

//	public MyGenConfig(String templatePath){
//		super(templatePath);
//	}
//
//	public List<CodeGenModel> codeGenModel = new ArrayList<CodeGenModel>();

	@Override
	public String getTemplate(String classPath) {
		try {
			//系统提供一个pojo模板
			InputStream ins = GenConfig.class.getResourceAsStream(classPath);
			if(ins==null) {
			    ClassLoader loader = Thread.currentThread().getContextClassLoader();
			    if(loader!=null) {
			        ins = loader.getResourceAsStream(classPath);
			    }
			}
			if(ins==null) {
			    throw new RuntimeException("未在classpath下找到Pojo模板文件 "+classPath);
			}
			InputStreamReader reader = new InputStreamReader(ins, "UTF-8");
			try{
				//todo, 根据长度来，不过现在模板不可能超过8k
				char[] buffer = new char[1024 * 70];
				int len = reader.read(buffer);
				return new String(buffer, 0, len);
			}finally {
				reader.close();
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	
}
