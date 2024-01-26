package com.wenge.datasource;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.StringTools;
import com.wenge.datasource.base.LogicDeleteInterceptor;
import com.wenge.datasource.gbase.GBase8sDialectConfig;
import com.wenge.datasource.mysql.MysqlDialectConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author HAO灏 2022/11/26 16:31
 */
@Configuration
public class MybatisPlusConfig {

    @Resource
    private DataSource dataSource;

    @Value("${mybatis-plus.global-config.db-config.logic-delete-field}")
    private String logicDeleteField;
    @Value("${mybatis-plus.global-config.db-config.logic-not-delete-value}")
    private String logicNotDeleteValue;
    @Value("${mybatis-plus.configuration.database-id}")
    private String databaseId;
    @Value("${external.tables}")
    private String externalTables;


    private final static Map<String, DialectConfig> DIALECT_CONFIG_MAP = CommonUtil.ofMap(
            DatasourceType.MySQL, MysqlDialectConfig.INSTANCE,
            DatasourceType.GBase8S, GBase8sDialectConfig.INSTANCE
    );

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() throws Exception {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DialectConfig dialectConfig = DIALECT_CONFIG_MAP.get(this.databaseId);
        dialectConfig.initTables(this.dataSource);
        dialectConfig.addInnerInterceptor(interceptor);
        dialectConfig.setCurrentDatasourceType();
        List<String> externalTableList = StringTools.convertToList(externalTables, ",");
        interceptor.addInnerInterceptor(new LogicDeleteInterceptor(this.logicDeleteField, this.logicNotDeleteValue, externalTableList));
        return interceptor;
    }


}
