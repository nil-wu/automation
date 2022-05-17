package com.gencode.gen;

import com.gencode.template.CodeGenModel;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.*;
import org.beetl.sql.core.kit.*;
import org.beetl.sql.ext.gen.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 生成dao代码.
 */

public class MySqlGenCode {
    // ========数据库配置=========
    // ========第三方供应商平台=========
    // jdbc:oracle:thin:@10.1.108.41:1521/coredev   clmrule/clmev14ruble
    // jdbc:oracle:thin:@10.1.108.42:1521/coresit   clmrule/clmqd96ihbe  clmopr/clmqd96cgbr zx_test/zx_test
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://10.1.23.10:6033/third_party_supplier_dev?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowMultiQueries=true";
    private static String userName = "third_party_supplier_dev";// clmrule/clmqd96ihbe  clmopr/clmqd96cgbr
    private static String password = "Aldp0982";

    // ========理赔=========
//    private static String driver = "oracle.jdbc.xa.client.OracleXADataSource";
//    private static String url = "jdbc:oracle:thin:@10.1.108.42:1521/coresit";
//    private static String userName = "zx_test";// clmrule/clmqd96ihbe  clmopr/clmqd96cgbr zx_test/zx_test
//    private static String password = "zx_test";

    // ========销管=========
//    private static String driver = "oracle.jdbc.xa.client.OracleXADataSource";
//    private static String url = "jdbc:oracle:thin:@10.1.108.43:1521/coreuat";
//    private static String userName = "salesmanage";// clmrule/clmqd96ihbe  clmopr/clmqd96cgbr
//    private static String password = "sman1410";


    // ========模板的路径, 示例是spring boot的[src/main/resources/beetlsqlTemplate 文件夹]=========
    private static String templatePath = "/beetlsqlTemplate/mybatis/huaan";
    // ========md生成路径 要提前创建=========
    private static String mdPath = "/sql";
    // ========生成实体类所在的包=========
    private static String pojoPkg = "com.test.entity";
    // ========生成mapper类所在的包=========
    private static String mapperPkg = "com.test.dao";
    // ========生成实体类所在的包=========
    private static String pojoDtoBasePkg = "com.core.application.claim.dto.domain.base";
    // ========生成实体类所在的包=========
    private static String pojoDtoPkg = "com.core.application.claim.dto.domain";

    /**
     * 入口
     */
    public static void main(String[] args) throws Exception {
        //表名
        String tableName = "t_tps_order_configuration";
//        String tableName = "PM_FACTORY_BRAND_DETAIL";

        //执行生成文件
        //proFlag 是否实际输出到项目路径上，如为false，只输出到本身项目
        genMybatis(tableName,false);
    }

    /**
     * 生成mybatis的相关数据库文件
     * @param table
     * @param proFlag 是否实际输出到项目
     * @throws Exception
     */
    public static void genMybatis(String table,boolean proFlag) throws Exception {
        //准备工作
        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, userName, password);
//        DBStyle mysql = new MySqlStyle();
        DBStyle mysql = new OracleStyle();
        SQLLoader loader = new ClasspathLoader(mdPath);
        SQLManager sqlManager = new SQLManager(mysql, loader, source, new UnderlinedNameConversion(), null);

        GenConfig config = new GenConfig(templatePath + "/Entity.btl");
        config.setDisplay(false);
        config.setPreferBigDecimal(false);
        config.setImplSerializable(false);
        config.setIgnorePrefix("t_");//表名要忽略的前缀，如果是 t_ 开头，忽略 t_

        String javaPath = GenKit.getJavaSRCPath();
        String hbmxmlPath = GenKit.getJavaResourcePath();

        List<CodeGenModel> codeGenModelList = new ArrayList<CodeGenModel>();

        //包名 + 模板 + 路径地址 + 文件名
        //Entity EntityKey
        CodeGenModel codeGenModel1 = new CodeGenModel();
        codeGenModel1.setPkg("cn.com.sinosafe.tps.module.repair.entity");//包名
        codeGenModel1.setFileName("Entity");//保存文件名的后缀，文件名：关键字 + 这个后缀
        codeGenModel1.setTemplateName(templatePath + "/Entity.btl");//模板名称
        //保存路径 如果proFlag为ture，保存到实际项目路径，不然只保存本项目路径
        codeGenModel1.setSrcPath(javaPath);
        codeGenModelList.add(codeGenModel1);

        //provier
        CodeGenModel codeGenModel2 = new CodeGenModel();
        codeGenModel2.setPkg("cn.com.sinosafe.tps.module.repair.provider");
        codeGenModel2.setFileName("Provider");
        codeGenModel2.setTemplateName(templatePath + "/Provider.btl");
        codeGenModel2.setSrcPath(javaPath);
        codeGenModelList.add(codeGenModel2);

        //Mapper.xml
        CodeGenModel codeGenModel8 = new CodeGenModel();
        codeGenModel8.setPkg("mapper");//非JAVA文件该字段没用
        codeGenModel8.setFileName("mapperxml");
        codeGenModel8.setTemplateName(templatePath + "/Mapper.btl");
        codeGenModel8.setSrcPath(hbmxmlPath);
        if (proFlag) {
            codeGenModel8.setSrcPath("H:/code/clm/dev/clm-web/src/main/resources");
        }
        codeGenModelList.add(codeGenModel8);

        /*如果proFlag为ture，保存到实际项目路径，不然只保存本项目路径*/
        if (proFlag) {
            for(CodeGenModel codeGenModel:codeGenModelList){
                codeGenModel1.setSrcPath("H:/code/clm/dev/clm-dto/src/main/java");
            }
        }
        config.codeGenModel = codeGenModelList;

        System.out.println("======生成开始======");

        //生成代码
        MybatisGen gen = new MybatisGen(sqlManager, table, pojoDtoBasePkg, GenKit.getJavaSRCPath(), config);
        gen.gen();

        System.out.println("=====生成完毕=====");
    }


    public static void genAll() throws Exception {
        //准备工作
        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, userName, password);
        DBStyle mysql = new MySqlStyle();
        SQLLoader loader = new ClasspathLoader(mdPath);
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, null);

        GenConfig config = new GenConfig();
        config.setDisplay(false);
        config.setPreferBigDecimal(true);

        System.out.println("======生成代码======");
        Set<String> tables = sqlManager.getMetaDataManager().allTable();
        for (String table : tables) {
            System.out.printf("%-20s %s\n",table , "生成完毕");
            //默认生成实体类的实现
            sqlManager.genPojoCode(table, pojoPkg, config);
            //自定义实现
            genMd(sqlManager, config, table);
            //自定义实现
            genMapper(sqlManager, config, table);
        }
        System.out.println("=====生成完毕=====");
    }

    /**
     * 生成md文件
     */
    public static void genMd(SQLManager sqlManager, GenConfig config, String table) throws IOException {
        String fileName = StringKit.toLowerCaseFirstOne(sqlManager.getNc().getClassName(table));
        if (config.getIgnorePrefix() != null && !config.getIgnorePrefix().trim().equals("")) {
            fileName = fileName.replaceFirst(StringKit.toLowerCaseFirstOne(config.getIgnorePrefix()), "");
            fileName = StringKit.toLowerCaseFirstOne(fileName);
        }
        String target = GenKit.getJavaResourcePath() + "/" + mdPath + "/" + fileName + ".md";
        TableDesc desc = sqlManager.getMetaDataManager().getTable(table);
        FileWriter writer = new FileWriter(new File(target));
        MDCodeGen mdCodeGen = new MDCodeGen();
        mdCodeGen.setMapperTemplate(config.getTemplate(templatePath + "/md.btl"));
        mdCodeGen.genCode(sqlManager.getBeetl(), desc, sqlManager.getNc(), null, writer);
        writer.close();
    }

    /**
     * 生成mapper
     */
    public static void genMapper(SQLManager sqlManager, GenConfig config, String table) {
        MapperCodeGen mapperCodeGen = new MapperCodeGen(mapperPkg);
        mapperCodeGen.setMapperTemplate(config.getTemplate(templatePath + "/mapper.btl"));
        mapperCodeGen.genCode(pojoPkg, sqlManager.getNc().getClassName(table), sqlManager.getMetaDataManager().getTable(table), null, false);
    }
}