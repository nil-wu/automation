package com.gencode.template;

import org.beetl.core.Template;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.NameConversion;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.engine.Beetl;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NameCoverCodeGen {

    private String mapperTemplate = "";

    public NameCoverCodeGen() {
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

    public void genCode(Beetl beetl, TableDesc tableDesc, NameConversion nc, String alias, Writer writer) {

        Set<String> cols = tableDesc.getCols();
        List<Map> attrs = new ArrayList<Map>();
        for (String col : cols) {

            ColDesc desc = tableDesc.getColDesc(col);
            Map attr = new HashMap();
            attr.put("colName", desc.colName); // 字段（列）名称
            attr.put("comment", desc.remark);
            String attrName = nc.getPropertyName(null, desc.colName);
            attr.put("name", attrName);
            attr.put("methodName", getMethodName(attrName));

            boolean isKey = tableDesc.getIdNames().contains(desc.colName);
            attr.put("isKey", isKey);

            String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
            if (type.equals("Timestamp")) {
                type = "Date";
            }

            attr.put("type", type);
            attr.put("desc", desc);

            if ("updated_by,date_created,date_updated,created_by,dateCreated,dateUpdated,createdBy,updatedBy".indexOf(attrName) > -1) {

            }else{
                attrs.add(attr);
            }
        }

        Template template = SourceGen.getGt().getTemplate(mapperTemplate);

        template.binding("tableName", tableDesc.getName());
        template.binding("cols", tableDesc.getCols());
        template.binding("idNames", tableDesc.getIdNames());
        template.binding("nc", nc);
        template.binding("alias", alias);
        template.binding("PS", beetl.getPs().getProperty("DELIMITER_PLACEHOLDER_START"));
        template.binding("PE", beetl.getPs().getProperty("DELIMITER_PLACEHOLDER_END"));
        template.binding("SS", beetl.getPs().getProperty("DELIMITER_STATEMENT_START"));
        template.binding("SE", beetl.getPs().getProperty("DELIMITER_STATEMENT_END"));
        template.binding("attrs",attrs);

        template.renderTo(writer);

    }

    private String getMethodName(String name) {
        if (name.length() == 1) {
            return name.toUpperCase();
        }
        char ch1 = name.charAt(0);
        char ch2 = name.charAt(1);
        if (Character.isLowerCase(ch1) && Character.isUpperCase(ch2)) {
            //aUname---> getaUname();
            return name;
        } else if (Character.isUpperCase(ch1) && Character.isUpperCase(ch2)) {
            //ULR --> getURL();
            return name;
        } else {
            //general  name --> getName()
            char upper = Character.toUpperCase(ch1);
            return upper + name.substring(1);
        }
    }

}
