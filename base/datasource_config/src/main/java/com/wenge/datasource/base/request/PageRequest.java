package com.wenge.datasource.base.request;

import com.wenge.common.util.CommonUtil;
import com.wenge.datasource.SqlWhereStringJoiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HAO灏 2020/8/24 13:56
 */
@Getter
@Setter
@Schema
public abstract class PageRequest {

    @Schema(description = "页码")
    private int page = 1;

    @Schema(description = "每页条数")
    private int page_size = 10;

    @Schema(description = "排序")
    private List<OrderBy> order_by;
    @Schema(hidden = true)
    private String group_by;
    @Schema(hidden = true)
    private Map<String, Object> params = new HashMap<>();

    @Schema(hidden = true)
    public String getOrderBy() {
        if (CommonUtil.isEmpty(this.order_by)) {
            return "";
        }
        return this.order_by.stream()
                .filter(orderBy -> CommonUtil.isNotBlank(orderBy.getColumn_name()))
                .map(orderBy -> orderBy.getColumn_name() + (orderBy.isAsc() ? " ASC" : " DESC"))
                .collect(Collectors.joining(",", " ORDER BY ", ""));
    }

    @Schema(hidden = true)
    public String getWhere() {
        String result = this.getWhere0();
        result += this.getGroupBy();
        result += this.getOrderBy();
        return result;
    }

    @Schema(hidden = true)
    public String getWhere0() {
        String result = "";
        SqlWhereStringJoiner where = new SqlWhereStringJoiner();

        this.addCondition(where);
        this.addCondition(where, this.params);
        if (CommonUtil.isNotBlank(where.toString())) {
            this.addParams(this.params);
            result += " WHERE " + where;
        }
        return result;
    }

    @Schema(hidden = true)
    private String getGroupBy() {
        if (CommonUtil.isBlank(this.group_by)) {
            return "";
        }
        return " " + this.group_by;
    }

    @Schema(hidden = true)
    public void addCondition(SqlWhereStringJoiner where) {
    }

    @Schema(hidden = true)
    public void addCondition(SqlWhereStringJoiner where, Map<String, Object> params) {
    }

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        return this.params;
    }

    @Schema(hidden = true)
    public void addParams(Map<String, Object> params) {
    }

}
