package com.gencode.gen;

import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.beetl.core.Template;
import org.beetl.sql.core.engine.Beetl;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import com.hahacs.dto.EstLoss2020Dto;

/**
 * 自动生成估损数据
 */
public class EstLossSqlGen {

	private String mapperTemplate = "";

	public EstLossSqlGen() {
		this.mapperTemplate = new GenConfig().getTemplate("/org/beetl/sql/ext/gen/md.btl");
	}

	public String getMapperTemplate() {
		return mapperTemplate;
	}

	/**
	 * 提供一个模板，否则使用默认的"/org/beetl/sql/ext/gen/md.btl"
	 * @param mapperTemplate
	 */
	public void setMapperTemplate(String mapperTemplate) {
		this.mapperTemplate = mapperTemplate;
	}

	public void genCode(String riskcode,String companyName, Map<String,List<String>> resultMap,Writer writer) {

		Template template = SourceGen.getGt().getTemplate(mapperTemplate);

		template.binding("riskcode", riskcode);
		template.binding("companyName", companyName);
		
		template.binding("jqList",(List<String>)resultMap.get("jqList"));
		template.binding("jq15List",(List<String>)resultMap.get("jq15List"));
		template.binding("jq30List",(List<String>)resultMap.get("jq30List"));
		template.binding("syList",(List<String>)resultMap.get("syList"));
		template.binding("sy30List",(List<String>)resultMap.get("sy30List"));
		template.binding("sy15List",(List<String>)resultMap.get("sy15List"));
		template.cf.setDirectByteOutput(true);
		template.cf.BUFFER_SIZE = "10000";
		template.cf.BUFFER_NUM = "100";
		
		template.renderTo(writer);
	}
	
	public void genCode2(String riskcode,String companyName, Map<String,List<String>> resultMap,Writer writer) {

		Template template = SourceGen.getGt().getTemplate(mapperTemplate);

		template.binding("riskcode", riskcode);
		template.binding("companyName", companyName);
		
		template.binding("jqtList",(List<String>)resultMap.get("jqtList"));
		template.binding("jqt15List",(List<String>)resultMap.get("jqt15List"));
		template.binding("jqt30List",(List<String>)resultMap.get("jqt30List"));

		template.cf.setDirectByteOutput(true);
		template.cf.BUFFER_SIZE = "10000";
		template.cf.BUFFER_NUM = "100";
		
		template.renderTo(writer);
	}
	
	public void genCode3(Map<String,List<EstLoss2020Dto>> resultMap,Writer writer) {

		Template template = SourceGen.getGt().getTemplate(mapperTemplate);
		
		template.binding("attrs",(List<EstLoss2020Dto>)resultMap.get("attrs"));

		template.cf.setDirectByteOutput(true);
		template.cf.BUFFER_SIZE = "10000";
		template.cf.BUFFER_NUM = "100";
		
		template.renderTo(writer);
	}

}
