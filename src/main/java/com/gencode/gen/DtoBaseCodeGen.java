package com.gencode.gen;

import org.beetl.core.Template;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.GenKit;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.IOException;

public class DtoBaseCodeGen implements CodeGen {
    String pkg = null;
    private String mapperTemplate = "";

    public DtoBaseCodeGen() {
        mapperTemplate = new GenConfig().getTemplate("/org/beetl/sql/ext/gen/mapper.btl");

    }

    public DtoBaseCodeGen(String pkg) {
        this();
        this.pkg = pkg;
    }

    public String getMapperTemplate() {
        return mapperTemplate;
    }

    public void setMapperTemplate(String mapperTemplate) {
        this.mapperTemplate = mapperTemplate;
    }

    @Override
    public void genCode(String entityPkg, String entityClass, TableDesc tableDesc, GenConfig config, boolean isDisplay) {
        if (pkg == null) {
            pkg = entityPkg;
        }
        Template template = SourceGen.getGt().getTemplate(mapperTemplate);
        String mapperClass = entityClass + "Dao";
        template.binding("className", mapperClass);
        template.binding("package", pkg);
        template.binding("entityClass", entityClass);

        String mapperHead = "import " + entityPkg + ".*;" + SourceGen.CR;
        template.binding("imports", mapperHead);
        String mapperCode = template.render();
        if (isDisplay) {
            System.out.println(mapperCode);
        } else {
            try {
                SourceGen.saveSourceFile(GenKit.getJavaSRCPath(), pkg, mapperClass, mapperCode);
            } catch (IOException e) {
                throw new RuntimeException("mapper代码生成失败", e);
            }
        }
    }
}
