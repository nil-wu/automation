package com.hahacs.contro;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.beetl.sql.core.kit.GenKit;

import com.gencode.gen.EstLossSqlGen;
import com.gencode.template.MyGenConfig;
import com.gencode.util.ExcelUtils;
import com.hahacs.dto.EstLoss2020Dto;

/**
 * 生成dao代码.
 */

public class EstGenCode3 {
    // ========模板的路径, 示例是spring boot的[src/main/resources/beetlsqlTemplate 文件夹]=========
    private static String templatePath = "/beetlsqlTemplate";
    // ========md生成路径 要提前创建=========
    private static String mdPath = "/sql";

    /**
     * 入口
     */
    public static void main(String[] args) throws Exception {
//        genAll();
//        genOne("clm_doc_main");

    	genEstLoss();
        
    }

    
    /**
     * 生成md文件
     * @throws Exception 
     */
    public static void genEstLoss() throws Exception {
    	
    	MyGenConfig config = new MyGenConfig();
        config.setDisplay(false);
        config.setPreferBigDecimal(true);
        config.setImplSerializable(true);
    	   	
    	String fileName = "01_CLM_DML_WUHAOPENG_REQ-11589_20200825";
        String target = GenKit.getJavaResourcePath() + "/" + mdPath + "/" + fileName + ".sql";
        
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target),"GB2312")));
        
        EstLossSqlGen estLossSqlGen = new EstLossSqlGen();
        estLossSqlGen.setMapperTemplate(config.getTemplate(templatePath + "/EstLossSql3.btl"));//重置模板
        estLossSqlGen.genCode3(getEstLossValue2020(), out);
        out.close();
    }
    
    private static Map<String,List<EstLoss2020Dto>> getEstLossValue2020() throws Exception{
		Map<String,List<EstLoss2020Dto>> resultMap = new HashMap<String,List<EstLoss2020Dto>>();
		
		String filePath = "G:\\whpcode\\huaan\\svn\\claim\\脚本\\2020未决赋值调整\\未决赋值-程序版.xls";
		List<String[]> allList = ExcelUtils.readExcel(filePath, "Sheet1", 59);
		List<EstLoss2020Dto> attrs = new ArrayList<EstLoss2020Dto>();
		
		String[] lossitemtypesnameArr = allList.get(0);
		String[] riskcodeArr = allList.get(1);
		String[] kindcodeArr = allList.get(2);
		String[] lossitemtypesArr = allList.get(3);
		
		for(int i = 0;i<allList.size();i++){
			if(i>3){
				String[] singleLine = allList.get(i);
				for(int j=0;j < singleLine.length;j++ ){
					if(j>0){
						String comcode =singleLine[0].substring(0,4);
						
						String lossitemtypesSplit = lossitemtypesArr[j];
						String[] split = lossitemtypesSplit.split(",");
						for(String lossitemtypes:split){
							EstLoss2020Dto estLoss2020Dto = new EstLoss2020Dto();
							estLoss2020Dto.setLossitemtypes(lossitemtypes);
							estLoss2020Dto.setLossitemtypesname(lossitemtypesnameArr[j]);
							estLoss2020Dto.setRiskcode(riskcodeArr[j]);
							estLoss2020Dto.setKindcode(kindcodeArr[j]);
							estLoss2020Dto.setComcode(comcode);
							estLoss2020Dto.setExecutevalue(singleLine[j]);
							attrs.add(estLoss2020Dto);
						}
					}
				}
			}
		}
		
		resultMap.put("attrs", attrs);

		return resultMap;
	}
	
    
}