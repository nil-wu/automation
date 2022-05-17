package com.gencode.gen;

import com.gencode.template.CodeGenModel;
import com.gencode.template.MyNameConversion;
import com.gencode.template.NameCoverCodeGen;
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

public class OracleGenCode {
    // ========数据库配置=========
    // jdbc:oracle:thin:@10.1.108.41:1521/coredev   clmrule/clmev14ruble
    // jdbc:oracle:thin:@10.1.108.42:1521/coresit   clmrule/clmqd96ihbe  clmopr/clmqd96cgbr
    private static String driver = "oracle.jdbc.xa.client.OracleXADataSource";
    private static String url = "jdbc:oracle:thin:@10.1.108.42:1521/coresit";
//    private static String url = "jdbc:oracle:thin:@10.1.108.41:1521/coredev";
    private static String userName = "clmdata";// clmrule/clmqd96ihbe  clmopr/clmqd96cgbr
    private static String password = "clmqd96srva";
//    private static String password = "clmev14opcr";
    // ========模板的路径, 示例是spring boot的[src/main/resources/beetlsqlTemplate 文件夹]=========
    private static String templatePath = "/beetlsqlTemplate";
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
        String tableName = "CLM_CLAIM_EXCEPTION_CHECK";

        //先进行字段名称映射
        genNameConver(tableName);

        //执行生成文件
        //proFlag 是否实际输出到项目路径上，如为false，只输出到本身项目
        genOne(tableName,false);
    }

    /**
     * 先生成对应的字段映射java类
     * @param table
     */
    public static void genNameConver(String table)throws Exception{
        System.out.println("======生成代码======");
        //准备工作
        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, userName, password);
//        DBStyle mysql = new MySqlStyle();
        DBStyle mysql = new OracleStyle();
        SQLLoader loader = new ClasspathLoader(mdPath);
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, null);

        GenConfig config = new GenConfig();
        config.setDisplay(false);
        config.setPreferBigDecimal(true);

        String fileName = "";
//        String target = GenKit.getJavaResourcePath() + "/" + mdPath + "/" + fileName + ".md";
        String target = GenKit.getJavaSRCPath() + "/com/gencode/util/CodeTransferUtils.java";
        TableDesc desc = sqlManager.getMetaDataManager().getTable(table);
        FileWriter writer = new FileWriter(new File(target));
        NameCoverCodeGen nameCoverCodeGen = new NameCoverCodeGen();
        nameCoverCodeGen.setMapperTemplate(config.getTemplate(templatePath + "/nc.btl"));
        nameCoverCodeGen.genCode(sqlManager.getBeetl(), desc, sqlManager.getNc(), null, writer);
        writer.close();
        System.out.println("=====生成完毕=====");
    }

    /**
     * 生成某表的hibernate文件
     * @param table
     * @param proFlag 是否实际输出到项目
     * @throws Exception
     */
    public static void genOne(String table,boolean proFlag) throws Exception {
        //准备工作
        ConnectionSource source = ConnectionSourceHelper.getSimple(driver, url, userName, password);
//        DBStyle mysql = new MySqlStyle();
        DBStyle mysql = new OracleStyle();
        SQLLoader loader = new ClasspathLoader(mdPath);
        MyNameConversion nc = new MyNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader, source, nc, null);

        GenConfig config = new GenConfig(templatePath + "/DtoBase.btl");
        config.setDisplay(false);
        config.setPreferBigDecimal(false);
        config.setImplSerializable(true);

        String javaPath = GenKit.getJavaSRCPath();
        String hbmxmlPath = GenKit.getJavaResourcePath();

        List<CodeGenModel> codeGenModel = new ArrayList<CodeGenModel>();

        CodeGenModel codeGenModel1 = new CodeGenModel();
        codeGenModel1.setPkg(pojoDtoPkg);
        codeGenModel1.setFileName("Dto");
        codeGenModel1.setTemplateName(templatePath + "/Dto.btl");
        codeGenModel1.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel1.setSrcPath("H:/code/clm/dev/clm-dto/src/main/java");
        }
        codeGenModel.add(codeGenModel1);

        CodeGenModel codeGenModel2 = new CodeGenModel();
        codeGenModel2.setPkg("com.core.application.claim.persistence.dao");
        codeGenModel2.setFileName("Dao");
        codeGenModel2.setTemplateName(templatePath + "/Dao.btl");
        codeGenModel2.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel2.setSrcPath("H:/code/clm/dev/clm-dao/src/main/java");
        }
        codeGenModel.add(codeGenModel2);

        CodeGenModel codeGenModel3 = new CodeGenModel();
        codeGenModel3.setPkg("com.core.application.claim.persistence.hibernate");
        codeGenModel3.setFileName("DaoHibernateImpl");
        codeGenModel3.setTemplateName(templatePath + "/DaoHibernateImpl.btl");
        codeGenModel3.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel3.setSrcPath("H:/code/clm/dev/clm-dao/src/main/java");
        }
        codeGenModel.add(codeGenModel3);

        CodeGenModel codeGenModel4 = new CodeGenModel();
        codeGenModel4.setPkg("com.core.application.claim.persistence.hibernate.base");
        codeGenModel4.setFileName("DaoHibernateImplBase");
        codeGenModel4.setTemplateName(templatePath + "/DaoHibernateImplBase.btl");
        codeGenModel4.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel4.setSrcPath("H:/code/clm/dev/clm-dao/src/main/java");
        }
        codeGenModel.add(codeGenModel4);

        CodeGenModel codeGenModel5 = new CodeGenModel();
        codeGenModel5.setPkg("com.core.application.claim.service.facade");
        codeGenModel5.setFileName("Service");
        codeGenModel5.setTemplateName(templatePath + "/Service.btl");
        codeGenModel5.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel5.setSrcPath("H:/code/clm/dev/clm-service/src/main/java");
        }
        codeGenModel.add(codeGenModel5);

        CodeGenModel codeGenModel6 = new CodeGenModel();
        codeGenModel6.setPkg("com.core.application.claim.service.spring");
        codeGenModel6.setFileName("ServiceSpringImpl");
        codeGenModel6.setTemplateName(templatePath + "/ServiceSpringImpl.btl");
        codeGenModel6.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel6.setSrcPath("H:/code/clm/dev/clm-service/src/main/java");
        }
        codeGenModel.add(codeGenModel6);

        CodeGenModel codeGenModel7 = new CodeGenModel();
        codeGenModel7.setPkg("com.core.application.claim.web.action");
        codeGenModel7.setFileName("Action");
        codeGenModel7.setTemplateName(templatePath + "/Action.btl");
        codeGenModel7.setSrcPath(javaPath);
        if (proFlag) {
            codeGenModel7.setSrcPath("H:/code/clm/dev/clm-web/src/main/java");
        }
        codeGenModel.add(codeGenModel7);

        CodeGenModel codeGenModel8 = new CodeGenModel();
        codeGenModel8.setPkg("com.core.application.claim.persistence.hibernate.mapping");
        codeGenModel8.setFileName("hbmxml");
        codeGenModel8.setTemplateName(templatePath + "/hbmxml.btl");
        codeGenModel8.setSrcPath(hbmxmlPath);
        if (proFlag) {
            codeGenModel8.setSrcPath("H:/code/clm/dev/clm-web/src/main/resources");
        }
        codeGenModel.add(codeGenModel8);

        config.codeGenModel = codeGenModel;

        System.out.println("======生成代码======");

        //默认生成实体类的实现
        if (proFlag) {
            sqlManager.genPojoCode(table, pojoDtoBasePkg, "H:/code/clm/dev/clm-dto/src/main/java",config);
        }else{
            sqlManager.genPojoCode(table, pojoDtoBasePkg, config);
        }

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