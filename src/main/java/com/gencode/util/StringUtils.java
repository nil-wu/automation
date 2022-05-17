package com.gencode.util;

public class StringUtils {

	
	/**
	 * 根据路径名获取文件名
	 * @param pathName
	 * @return
	 */
	public static String getFileName(String pathName){
		String fileName = "";
		fileName = pathName.substring(pathName.lastIndexOf("\\")+1, pathName.lastIndexOf("."));
		System.out.println(fileName);
		return fileName;
	}
}
