package com.wenge.datasource.mysql;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
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

public class MysqlDialectConfig implements DialectConfig {

    public final static MysqlDialectConfig INSTANCE = new MysqlDialectConfig();

    @Override
    public void initTables(DataSource dataSource) throws SQLException {
        Map<String, DataSource> dataSourceMap = ((DynamicRoutingDataSource) dataSource).getDataSources();
        String sql = "SELECT" +
                " TABLE_NAME," +
                " count( PARTITION_NAME ) AS partition_num " +
                " FROM" +
                " information_schema.PARTITIONS " +
                " WHERE" +
                " TABLE_SCHEMA = ? " +
//				" AND PARTITION_NAME IS NOT NULL " +
                " GROUP BY" +
                " TABLE_NAME";
        for (Map.Entry<String, DataSource> entry : dataSourceMap.entrySet()) {
            try (Connection connection = entry.getValue().getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                String databaseName = connection.getCatalog();
                String dataSourceName = entry.getKey();
                preparedStatement.setString(1, databaseName);
                PartitionManager.registerDatabase(dataSourceName, databaseName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        PartitionManager.registerPartition(dataSourceName, resultSet.getString(1), resultSet.getInt(2));
                    }
                }
            }
        }
    }

    @Override
    public void addInnerInterceptor(MybatisPlusInterceptor interceptor) {
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //添加上自己实现的表名处理器
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler(new PartitionTableNameHandler());
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
    }

    @Override
    public void setCurrentDatasourceType() {
        DatasourceType.setCurrentDatasourceType(DatasourceType.MySQL);
    }
}
