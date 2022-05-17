package com.gencode.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 共用代码转化类
 */
public class CodeTransferUtils {

    private CodeTransferUtils(){}

    public String convertCode(String code){return "";}

    public static CodeTransferUtils Convert_Name = new Convert_Name();

    private static class Convert_Name extends CodeTransferUtils {
        private static Map<String, String> codeMap = new HashMap<String, String>();

        static{
            codeMap.put("registno","registno");
            codeMap.put("claimno","claimno");
            codeMap.put("policyno","policyno");
            codeMap.put("riskcode","riskcode");
            codeMap.put("companycode","companycode");
            codeMap.put("adjustesttimes","adjustesttimes");
            codeMap.put("estloss","estloss");
            codeMap.put("sumestlosschange","sumestlosschange");
            codeMap.put("sumandchangeflag","sumandchangeflag");
            codeMap.put("repairflag","repairflag");

            codeMap.put("date_updated","dateUpdated");
            codeMap.put("created_by","createdBy");
            codeMap.put("date_created","dateCreated");
            codeMap.put("updated_by","updatedBy");
        }

        @Override
        public String convertCode(String code) {
            if(code == null || "".equals(code)){
                return "";
            }
            return codeMap.get(code);
        }
    }
}