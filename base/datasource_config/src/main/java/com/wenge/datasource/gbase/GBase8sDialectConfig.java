package com.wenge.datasource.gbase;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.wenge.datasource.DatasourceType;
import com.wenge.datasource.DialectConfig;
import com.wenge.datasource.base.PartitionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GBase8sDialectConfig implements DialectConfig {

    public final static GBase8sDialectConfig INSTANCE = new GBase8sDialectConfig();

    @Override
    public void initTables(DataSource dataSource) throws SQLException {
        Map<String, DataSource> dataSourceMap = ((DynamicRoutingDataSource) dataSource).getDataSources();
        String sql = "select tabname AS table_name,0 AS partition_num from systables where tabid > 99";
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            try (Connection connection = entry.getValue().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        PartitionManager.registerPartition(entry.getKey(), resultSet.getString(1), resultSet.getInt(2));
                    }
                }
            }
        }
    }

    @Override
    public void addInnerInterceptor(MybatisPlusInterceptor interceptor) {
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.GBASE_8S));
    }

    @Override
    public void setCurrentDatasourceType() {
        DatasourceType.setCurrentDatasourceType(DatasourceType.GBase8S);
    }

}
