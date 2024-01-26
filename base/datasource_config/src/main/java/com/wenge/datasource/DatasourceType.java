package com.wenge.datasource;

import com.wenge.common.exception.BusinessException;
import com.wenge.common.util.CommonUtil;

import java.util.Map;

public class DatasourceType {

    public final static String MySQL = "mysql";
    public final static String GBase8S = "gbase-8s";

    private static String CURRENT_DATASOURCE_TYPE;

    public static void setCurrentDatasourceType(String currentDatasourceType) {
        CURRENT_DATASOURCE_TYPE = currentDatasourceType;
    }

    public static String getCurrentDatasourceType() {
        return CURRENT_DATASOURCE_TYPE;
    }

    public static String get(String... input) {
        Map<String, String> map = CommonUtil.ofMapN((Object[]) input);
        String s = map.get(DatasourceType.getCurrentDatasourceType());
        if (CommonUtil.isBlank(s)) {
            throw new BusinessException("get failed");
        }
        return s;
    }

}
