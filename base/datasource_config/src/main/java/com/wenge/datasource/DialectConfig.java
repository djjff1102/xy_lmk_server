package com.wenge.datasource;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;

import javax.sql.DataSource;
import java.sql.SQLException;

public interface DialectConfig {

    void initTables(DataSource dataSource) throws SQLException;

    default void addInnerInterceptor(MybatisPlusInterceptor interceptor) {
    }

    void setCurrentDatasourceType();

}
