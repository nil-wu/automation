package com.gencode.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtils {

	/**
	 * 导出excel文件
	 * @param pathName
	 * @param allData
	 * @throws Exception
	 */
	public static void exportExcel(String pathName,List<String[]> allData) throws Exception{
		String fileName = StringUtils.getFileName(pathName);
		
		Workbook wk=new HSSFWorkbook();//创建一个工作薄
        Sheet sh=wk.createSheet(fileName);//创建一个sheet页
        
        if(allData!=null && allData.size()>0){
        	for (int i = 0; i < allData.size(); i++) {
        		Row row=sh.createRow(i);//创建第一行
        		
        		String[] strings = allData.get(i);
        		for (int j = 0; j < strings.length; j++) {
        			Cell cell=row.createCell(j);
        			cell.setCellValue(strings[j]);
				}
			}
        }
        
        FileOutputStream out= new FileOutputStream("D:\\统计汇总.xls");
        wk.write(out);
        out.close();
	}
	
	
	public static List<String[]> readExcel(String pathName,String sheetName,int colNum)throws Exception{
		List<String[]> allData = new ArrayList<String[]>();
		//1、获取文件输入流
		InputStream inputStream = new FileInputStream(pathName);
		//2、获取Excel工作簿对象
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		//3、得到Excel工作表对象
		HSSFSheet sheetAt = workbook.getSheet(sheetName);
		
		//4、循环读取表格数据
		for (Row row : sheetAt) {
			//首行（即表头）不读取
//			if (row.getRowNum() == 0) {
//				continue;
//			}
			
			
			String[] rowData = new String[colNum];
			
			for(int i = 0;i<colNum;i++){
//				rowData[i]=row.getCell(i).getStringCellValue();
				String value = getCellStringValue(row.getCell(i));
//				if(i!=0){
//					value = getTrueData(value);
//				}
				rowData[i]=value;
				
			}
			
			allData.add(rowData);
		}
		//5、关闭流
		workbook.close();
		return allData;
	}
	
	public static String getTrueData(String value){
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	private static String getCellStringValue(Cell cell) { 
		String cellValue = ""; 
		switch (cell.getCellType()) { 
			case HSSFCell.CELL_TYPE_STRING://字符串类型 
				cellValue = cell.getStringCellValue(); 
				if(cellValue.trim().equals("")||cellValue.trim().length()<=0) 
					cellValue=" "; 
				break; 
			case HSSFCell.CELL_TYPE_NUMERIC: //数值类型 
				cellValue = String.valueOf(cell.getNumericCellValue());
				cellValue = new BigDecimal(cellValue).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
				break; 
			case HSSFCell.CELL_TYPE_FORMULA: //公式 
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC); 
				cellValue = String.valueOf(cell.getNumericCellValue()); 
				break; 
			case HSSFCell.CELL_TYPE_BLANK: 
				cellValue=" "; 
				break; 
			case HSSFCell.CELL_TYPE_BOOLEAN: 
				break; 
			case HSSFCell.CELL_TYPE_ERROR: 
				break; 
			default: 
				break; 
		} 
		return cellValue; 
	}
	
}
