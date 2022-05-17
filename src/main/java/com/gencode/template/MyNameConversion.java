package com.gencode.template;

import com.gencode.util.CodeTransferUtils;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.annotatoin.Table;
import org.beetl.sql.core.kit.StringKit;

import java.util.HashMap;
import java.util.Map;

public class MyNameConversion extends NameConversion {

    @Override
    public String getTableName(Class<?> c) {
        Table table = (Table) c.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }
        return StringKit.enCodeUnderlined(c.getSimpleName());
    }

    public String getClassName(String tableName) {
        String temp = StringKit.deCodeUnderlined(tableName.toLowerCase());
        return StringKit.toUpperCaseFirstOne(temp);

    }

    @Override
    public String getColName(Class<?> c, String attrName) {

        return StringKit.enCodeUnderlined(attrName);
    }


    @Override
    public String getPropertyName(Class<?> c, String colName) {
        return CodeTransferUtils.Convert_Name.convertCode(colName.toLowerCase());
    }

}
