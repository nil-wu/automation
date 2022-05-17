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
import java.sql.JDBCType;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器
 */
public class MybatisGen {
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

    public MybatisGen(SQLManager sm, String table, String pkg, String srcPath, GenConfig config) {
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
        if (className.contains("mapperxml")) {
            className = className.replace("hbmxml","");
            fileType = "Mapper.xml";
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

        //绑定的模板键值对的数据
        Map<String, Object> attrMap = new HashMap<>();

        //import 是否包含某些特殊类
        boolean hasDate = false;

        final TableDesc tableDesc = mm.getTable(table);

        //真正表名 t_tps_repair_record
        String tableNameReal = tableDesc.getName();
        //去除某些前缀的表名，后期字段关键字名称根据这个来 tps_repair_record
        String tableName = tableNameReal;
        if (config.getIgnorePrefix() != null && !config.getIgnorePrefix().trim().equals("")) {
            tableName = tableName.replaceFirst(config.getIgnorePrefix(), "");
        }

        //关键字 TpsRepairRecord
        String className = sm.getNc().getClassName(tableName);
        String keywords = sm.getNc().getClassName(tableName);

        //首字母为小写的关键字 tpsRepairRecord
        String keywordsLowerFirstOne = StringKit.toLowerCaseFirstOne(keywords);

        //POJO是否分离，如果主键大于1个，pojo的主键单独成类
        boolean hasSpite = false;
        //继承类
        String ext = null;
        //
        String parameterType = "";
        if (tableDesc.getIdNames().size() > 1) {
            hasSpite = true;
            ext = keywords + "EntityKey";
            parameterType = "cn.com.sinosafe.tps.module.repair.entity."+ ext;
        }

        Set<String> cols = tableDesc.getCols();
        List<Map> attrs = new ArrayList<Map>();//全部字段
        List<Map> attrsKey = new ArrayList<Map>();//主键
        for (String col : cols) {
            ColDesc desc = tableDesc.getColDesc(col);
            Map attr = new HashMap();
            attr.put("colName", desc.colName); // 字段（列）名称
            attr.put("comment", desc.remark);
            String attrName = sm.getNc().getPropertyName(null, desc.colName);//去掉下横杠，下横杠后首字母变为大写
            attr.put("name", attrName);
            attr.put("methodName", getMethodName(attrName));

            boolean isKey = tableDesc.getIdNames().contains(desc.colName);
            attr.put("isKey", isKey);

            attr.put("jdbcType", JDBCType.valueOf(desc.sqlType).getName());

            String type = JavaType.getType(desc.sqlType, desc.size, desc.digit);
            if (config.isPreferBigDecimal() && type.equals("Double")) {
                type = "BigDecimal";
            }
            if (config.isPreferDate() && type.equals("Timestamp")) {
                type = "Date";
                hasDate = true;
            }

            if("Long".equals(type) && attrName.toLowerCase().contains("indemnitydutyrate")){
                type = "Integer";
            }
            attr.put("type", type);
            attr.put("lowertype", type.toLowerCase());
            attr.put("desc", desc);

            if (!hasSpite) {
                if (isKey) {
                    parameterType = "java.lang." + type;
                }
            }

            attrs.add(attr);

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

        //import 导入类
        StringBuffer t = new StringBuffer();
        if (hasDate) {
            t.append("import java.util.Date;");
            t.append(CR);
        }
        srcHead = t.toString();

//        String attrsKeyString = "";
//        String attrsKeyString2 = "";
//        String attrsKeyString3 = "";
//        for (Map attr : attrsKey) {
//            attrsKeyString = attrsKeyString + "," + attr.get("type")+" "+attr.get("name");
//            attrsKeyString2 = attrsKeyString2 + "," +attr.get("name");
//            attrsKeyString3 = attrsKeyString3 + ",\"" +attr.get("name")+"\"";
//        }
//        attrsKeyString = attrsKeyString.substring(1);
//        attrsKeyString2 = attrsKeyString2.substring(1);
//        attrsKeyString3 = attrsKeyString3.substring(1);

        String attrsKeyString4 = "";
        String attrsKeyString6 = "";
        String attrsKeyString7 = "";
        int length = 0;
        int length2 = 0;
        for (Map attr : attrs) {
            attrsKeyString4 = attrsKeyString4 + "," +attr.get("colName")+"";
            length = length + String.valueOf(attr.get("colName")).length();
            if (length > 100) {
                attrsKeyString4 = attrsKeyString4 + "\n\t";
                length = 0;
            }

            attrsKeyString6 = attrsKeyString6 + ", #{" + attr.get("name") + ",jdbcType=" + attr.get("jdbcType") + "}";
            length2 = length2 + String.valueOf(", #{" + attr.get("name") + ",jdbcType=" + attr.get("jdbcType") + "}").length();
            if (length2 > 100) {
                attrsKeyString6 = attrsKeyString6 + "\n\t";
                length2 = 0;
            }

            attrsKeyString7 = attrsKeyString7 + "," + attr.get("colName") + " = #{" + attr.get("name") + ",jdbcType=" + attr.get("jdbcType") + "}";
            attrsKeyString7 = attrsKeyString7 + "\n\t";
        }
        attrsKeyString4 = attrsKeyString4.substring(1);
        attrsKeyString6 = attrsKeyString6.substring(1);
        attrsKeyString7 = attrsKeyString7.substring(1);

        String attrsKeyString5 = "";
        for (Map attr : attrsKey) {
            attrsKeyString5 = attrsKeyString5 + "and "+attr.get("colName")+" = #{"+attr.get("name")+",jdbcType="+attr.get("jdbcType")+"}";
            attrsKeyString5 = attrsKeyString5 + "\n\t";
        }
        if (attrsKeyString5.length() > 4) {
            attrsKeyString5 = attrsKeyString5.substring(4);
        }

        //获取当下时间
        Template template = gt.getTemplate(config.getTemplate());
        Date now=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(f.format(now));

        attrMap.put("tableName", tableName);//去除前缀的表名 tps_repair_record
        attrMap.put("tableNameReal", tableNameReal);//表名 t_tps_repair_record
        attrMap.put("attrs", attrs);//属性列表
        attrMap.put("attrsKey", attrsKey);//主键属性列表
        attrMap.put("ext", ext);//继承类字符串
        attrMap.put("today", f.format(now));//当下时间字符串
        attrMap.put("keywords", keywords);//关键字 TpsRepairRecord
        attrMap.put("hasSpite", hasSpite);//关键字 TpsRepairRecord
        attrMap.put("implSerializable", config.isImplSerializable());//是否implements Serializable
        attrMap.put("imports", srcHead);//import
        attrMap.put("attrsKeyString4", attrsKeyString4);//全部字段表（有下横杠）字符串
        attrMap.put("parameterType", parameterType);
        attrMap.put("attrsKeyString5", attrsKeyString5);
        attrMap.put("attrsKeyString6", attrsKeyString6);
        attrMap.put("attrsKeyString7", attrsKeyString7);
        attrMap.put("keywordsLowerFirstOne", keywordsLowerFirstOne);

//        attrMap.put("attrsKeyString", attrsKeyString);
//        attrMap.put("attrsKeyString2", attrsKeyString2);
//        attrMap.put("attrsKeyString3", attrsKeyString3);


        attrMap.put("className", className);
        attrMap.put("table", trimCategory(table));
        attrMap.put("comment", tableDesc.getRemark());
        attrMap.put("catalog", tableDesc.getCatalog());


        attrMap.put("lowerClassName", keywordsLowerFirstOne);

        template.binding("tableName", tableName);
        template.binding("className", className);
        template.binding("lowerClassName", keywordsLowerFirstOne);
        template.binding("table", trimCategory(table));
        template.binding("comment", tableDesc.getRemark());
        template.binding("catalog", tableDesc.getCatalog());

//        String code = template.render();
//        if (config.isDisplay()) {
//            System.out.println(code);
//        } else {
//            saveSourceFile(srcPath, pkg, fileName, code);
//        }

//        for (CodeGen codeGen : config.codeGens) {
//            codeGen.genCode(pkg, className, tableDesc, config, config.isDisplay());
//        }
        if (hasSpite) {
            CodeGenModel codeGenModel1 = new CodeGenModel();
            codeGenModel1.setPkg("cn.com.sinosafe.tps.module.repair.entity");//包名
            codeGenModel1.setFileName("EntityKey");//保存文件名的后缀，文件名：关键字 + 这个后缀
            codeGenModel1.setTemplateName("/beetlsqlTemplate/mybatis/huaan/EntityKey.btl");//模板名称
            //保存路径 如果proFlag为ture，保存到实际项目路径，不然只保存本项目路径
            codeGenModel1.setSrcPath(config.codeGenModel.get(0).getSrcPath());
            config.codeGenModel.add(codeGenModel1);
        }

        for (CodeGenModel codeGen : config.codeGenModel) {
            genEach(codeGen,attrMap,keywords);
        }

    }


    private void genEach(CodeGenModel codeGen,Map<String, Object> attrMap,String keywords)throws Exception{
        System.out.println("======生成开始：" + keywords + codeGen.getFileName() + "======");

        Template template = gt.getTemplate(new GenConfig().getTemplate(codeGen.getTemplateName()));

        for(Map.Entry<String, Object> entry : attrMap.entrySet()){
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            template.binding(mapKey, mapValue);
        }
        template.binding("package", codeGen.getPkg());
        String resultStr = template.render();
        if (config.isDisplay()) {
            System.out.println(resultStr);
        } else {
            saveSourceFile(codeGen.getSrcPath(), codeGen.getPkg(), keywords + codeGen.getFileName(), resultStr);
        }

        System.out.println("======生成结束:" + keywords + codeGen.getFileName() + "======");
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

