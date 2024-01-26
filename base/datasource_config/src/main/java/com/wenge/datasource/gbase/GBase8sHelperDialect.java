package com.wenge.datasource.gbase;

import com.github.pagehelper.Page;
import com.github.pagehelper.dialect.AbstractHelperDialect;
import com.github.pagehelper.util.MetaObjectUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMapping.Builder;
import org.apache.ibatis.reflection.MetaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GBase8sHelperDialect extends AbstractHelperDialect {

    public Object processPageParameter(MappedStatement ms, Map<String, Object> paramMap, Page page, BoundSql boundSql, CacheKey pageKey) {
        paramMap.put("First_PageHelper", page.getStartRow());
        paramMap.put("Second_PageHelper", page.getPageSize());
        pageKey.update(page.getStartRow());
        pageKey.update(page.getPageSize());
        if (boundSql.getParameterMappings() != null) {
            List<ParameterMapping> newParameterMappings = new ArrayList<>(boundSql.getParameterMappings());
            if (page.getStartRow() == 0L) {
                newParameterMappings.add(0,(new Builder(ms.getConfiguration(), "Second_PageHelper", Integer.TYPE)).build());
            } else {
                newParameterMappings.add(0,(new Builder(ms.getConfiguration(), "First_PageHelper", Long.TYPE)).build());
                newParameterMappings.add(1,(new Builder(ms.getConfiguration(), "Second_PageHelper", Integer.TYPE)).build());
            }

            MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
            metaObject.setValue("parameterMappings", newParameterMappings);
        }

        return paramMap;
    }

    public String getPageSql(String sql, Page page, CacheKey pageKey) {
        StringBuilder sqlBuilder = new StringBuilder();
        if (page.getStartRow() == 0L) {
            sql = sql.replaceFirst("^\\s+", "");
            String firstPart = sql.substring(0, 6).toLowerCase();
            String remainingPart = sql.substring(6);
            sql = firstPart + remainingPart;
            sql = sql.replaceFirst("select", "select skip 0 first ? ");
        } else {
            sql = sql.replaceFirst("^\\s+", "");
            String firstPart = sql.substring(0, 6).toLowerCase();
            String remainingPart = sql.substring(6);
            sql = firstPart + remainingPart;
            sql = sql.replaceFirst("select", "select skip ? first ? ");
        }
        sqlBuilder.append(sql);
        return sqlBuilder.toString();
    }
}
