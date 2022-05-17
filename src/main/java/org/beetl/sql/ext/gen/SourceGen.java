package org.beetl.sql.ext.gen;

import com.gencode.template.CodeGenModel;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.sql.core.JavaType;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.db.ColDesc;
import org.beetl.sql.core.db.MetadataManager;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.StringKit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器
 */
public class SourceGen {
    public static final String CR = System.getProperty("line.separator");
    /**
     * logger
     */
    public static String defaultPkg = "com.test";
    private static String srcHead;
    /**
     * 代码生成的Beetl的GroupTemplate，与BeetSQL 不同
     */
    private static GroupTemplate gt = null;

    static {
        Configuration conf = null;
        try {
            conf = Configuration.defaultConfiguration();
            conf.setStatementStart("<%");
            conf.setStatementEnd("%>");
        } catch (IOException e) {
            throw new RuntimeException("build defaultConfiguration error", e);
        }

        gt = new GroupTemplate(new StringTemplateResourceLoader(), conf);
        StringBuffer t = new StringBuffer();
        t.append("import java.math.*;");
        t.append(CR);
        t.append("import java.util.Date;");
        t.append(CR);
        t.append("import java.sql.Timestamp;");
        t.append(CR);
        t.append("import org.beetl.sql.core.annotatoin.Table;");
        t.append(CR);
        srcHead = t.toString();
    }

    private MetadataManager mm;
    private SQLManager sm;
    private String table;
    private String pkg;
    private String srcPath;
    private GenConfig config;

    public SourceGen(SQLManager sm, String table, String pkg, String srcPath, GenConfig config) {
        this.mm = sm.getMetaDataManager();
        this.sm = sm;
        this.table = table;
        this.pkg = pkg;
        this.srcPath = srcPath;
        this.config = config;
    }

    public static String getSrcHead() {
        return srcHead;
    }

    public static GroupTemplate getGt() {
        return gt;
    }

    public static void saveSourceFile(String srcPath, String pkg, String className, String content) throws IOException {
        String file = srcPath + File.separator + pkg.replace('.', File.separatorChar);
        File f = new File(file);
        if (!f.exists()) {
            boolean succ = f.mkdirs();
            if (!succ) {
                throw new IOException("创建文件夹失败 " + f);
            }
        }
        String fileType = ".java";
        if (className.contains("hbmxml")) {
            className = className.replace("hbmxml","");
            fileType = ".hbm.xml";
        }

        File target = new File(file, className + fileType);

        FileWriter writer = new FileWriter(target);
        try {
            writer.write(content);
        } finally {
            writer.close();
        }
    }

    /**
     * 生成代码
     *
     */
    public void gen() throws Exception {
        final TableDesc tableDesc = mm.getTable(table);
        String className = sm.getNc().getClassName(tableDesc.getName());
        if(className.contains("Clm")){
            className = className.replace("Clm", "Gc");
        }
        String fileName = className + "DtoBase";
        String lowerClassName = StringKit.toLowerCaseFirstOne(className);
        if (config.getIgnorePrefix() != null && !config.getIgnorePrefix().trim().equals("")) {
            className = className.replaceFirst(StringKit.toUpperCaseFirstOne(config.getIgnorePrefix()), "");
        }
        String ext = null;

        if (config.getBaseClass() != null) {
            ext = config.getBaseClass();
        }

        Set<String> cols = tableDesc.getCols();
        List<Map> attrs = new ArrayList<Map>();
        //主键
        List<Map> attrsKey = new ArrayList<Map>();
        for (String col : cols) {

            ColDesc desc = tableDesc.getColDesc(col);
            Map attr = new HashMap();
            attr.put("colName", desc.colName); // 字段（列）名称
            attr.put("comment", desc.remark);
            String attrName = sm.getNc().getPropertyName(null, desc.colName);
            attr.put("name", attrName);
            attr.put("methodName", getMethodName(attrName));

            boolean isKey = tableDesc.getIdNames().contains(desc.colName);
            attr.put("isKey", isKey);

            String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
            if (config.isPreferBigDecimal() && type.equals("Double")) {
                type = "BigDecimal";
            }
            if (config.isPreferDate() && type.equals("Timestamp")) {
                type = "Date";
            }

            if("Long".equals(type) && attrName.toLowerCase().contains("indemnitydutyrate")){
                type = "Integer";
            }
            attr.put("type", type);
            attr.put("lowertype", type.toLowerCase());
            attr.put("desc", desc);

//            if ("updated_by,date_created,date_updated,created_by".indexOf(attrName) > -1) {
//
//            }else{
                attrs.add(attr);
//            }

            if (isKey) {
                attrsKey.add(attr);
            }
        }

        if (config.getPropertyOrder() == GenConfig.ORDER_BY_TYPE) {
            // 主键总是排在前面，int类型也排在前面，剩下的按照字母顺序排
            Collections.sort(attrs, new Comparator<Map>() {

                @Override
                public int compare(Map o1, Map o2) {
                    ColDesc desc1 = (ColDesc) o1.get("desc");
                    ColDesc desc2 = (ColDesc) o2.get("desc");
                    int score1 = score(desc1);
                    int score2 = score(desc2);
                    if (score1 == score2) {
                        return desc1.colName.compareTo(desc2.colName);
                    } else {
                        return score2 - score1;
                    }


                }

                private int score(ColDesc desc) {
                    if (tableDesc.getIdNames().contains(desc.colName)) {
                        return 99;
                    } else if (JavaType.isInteger(desc.sqlType)) {
                        return 9;
                    } else if (JavaType.isDateType(desc.sqlType)) {
                        return -9;
                    } else {
                        return 0;
                    }
                }


            });
        }

        String attrsKeyString = "";
        String attrsKeyString2 = "";
        String attrsKeyString3 = "";
        for (Map attr : attrsKey) {
            attrsKeyString = attrsKeyString + "," + attr.get("type")+" "+attr.get("name");
            attrsKeyString2 = attrsKeyString2 + "," +attr.get("name");
            attrsKeyString3 = attrsKeyString3 + ",\"" +attr.get("name")+"\"";
        }
        attrsKeyString = attrsKeyString.substring(1);
        attrsKeyString2 = attrsKeyString2.substring(1);
        attrsKeyString3 = attrsKeyString3.substring(1);

        String attrsKeyString4 = "";
        int length = 0;
        for (Map attr : attrs) {
            attrsKeyString4 = attrsKeyString4 + ",\"" +attr.get("name")+"\"";
            length = length + String.valueOf(attr.get("name")).length();
            if (length > 100) {
                attrsKeyString4 = attrsKeyString4 + "\n\t\t";
                length = 0;
            }
        }
        attrsKeyString4 = attrsKeyString4.substring(1);

        String tableName = tableDesc.getName();

        Map<String, Object> attrMap = new HashMap<>();

        Template template = gt.getTemplate(config.getTemplate());
        Date now=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(f.format(now));
        template.binding("today",f.format(now));

        attrMap.put("attrs", attrs);
        attrMap.put("attrsKey", attrsKey);
        attrMap.put("attrsKeyString", attrsKeyString);
        attrMap.put("attrsKeyString2", attrsKeyString2);
        attrMap.put("attrsKeyString3", attrsKeyString3);
        attrMap.put("attrsKeyString4", attrsKeyString4);
        attrMap.put("tableName", tableName);
        attrMap.put("className", className);
        attrMap.put("table", trimCategory(table));
        attrMap.put("ext", ext);
//        attrMap.put("package", pkg);
        attrMap.put("imports", srcHead);
        attrMap.put("comment", tableDesc.getRemark());
        attrMap.put("catalog", tableDesc.getCatalog());
        attrMap.put("implSerializable", config.isImplSerializable());
        attrMap.put("today", f.format(now));
        attrMap.put("lowerClassName", lowerClassName);

        template.binding("attrs", attrs);
        template.binding("attrsKey", attrsKey);
        template.binding("attrsKeyString", attrsKeyString);
        template.binding("attrsKeyString2", attrsKeyString2);
        template.binding("attrsKeyString3", attrsKeyString3);
        template.binding("attrsKeyString4", attrsKeyString4);
        template.binding("tableName", tableName);
        template.binding("className", className);
        template.binding("lowerClassName", lowerClassName);
        template.binding("table", trimCategory(table));
        template.binding("ext", ext);
        template.binding("package", pkg);
        template.binding("imports", srcHead);
        template.binding("comment", tableDesc.getRemark());
        template.binding("catalog", tableDesc.getCatalog());
        template.binding("implSerializable", config.isImplSerializable());

        String code = template.render();
        if (config.isDisplay()) {
            System.out.println(code);
        } else {
            saveSourceFile(srcPath, pkg, fileName, code);
        }

        for (CodeGen codeGen : config.codeGens) {
            codeGen.genCode(pkg, className, tableDesc, config, config.isDisplay());
        }

        for (CodeGenModel codeGen : config.codeGenModel) {
            genMapper(codeGen,attrMap,className);
        }

    }

    private void genMapper(CodeGenModel codeGen,Map<String, Object> attrMap,String className)throws Exception{
        System.out.println("======生成开始："+className+codeGen.getFileName()+"======");

        Template template = gt.getTemplate(new GenConfig().getTemplate(codeGen.getTemplateName()));

        for(Map.Entry<String, Object> entry : attrMap.entrySet()){
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            template.binding(mapKey, mapValue);
        }
        template.binding("package", codeGen.getPkg());
        String code = template.render();
        if (config.isDisplay()) {
            System.out.println(code);
        } else {
            saveSourceFile(codeGen.getSrcPath(), codeGen.getPkg(), className+codeGen.getFileName(), code);
        }

        System.out.println("======生成结束:"+className+codeGen.getFileName()+"======");
    }

    private String trimCategory(String table) {
        int index = -1;
        if ((index = table.indexOf(".")) == -1) {
            return table;
        }
        return table.substring(index + 1);
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

