package com.wenge.datasource;

import com.mysql.cj.util.StringUtils;

import java.util.StringJoiner;

public class SqlWhereStringJoiner {

    private final StringJoiner where = new StringJoiner(" AND ");

    private String result;

    public void add(String whereSql) {
        this.where.add(whereSql);
    }

    public void add(String datasourceType, String whereSql) {
        if (!DatasourceType.getCurrentDatasourceType().equals(datasourceType)) {
            return;
        }
        this.where.add(whereSql);
    }

    public String toString() {
        return this.result == null ? this.result = this.where.toString() : this.result;
    }
}
