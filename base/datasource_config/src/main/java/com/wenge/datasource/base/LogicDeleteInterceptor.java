package com.wenge.datasource.base;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.wenge.common.config.log.Log;
import com.wenge.common.util.CommonUtil;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;
import java.util.List;

/**
 * @author HAO灏 2023/1/4 14:00
 */
@AllArgsConstructor
public class LogicDeleteInterceptor implements InnerInterceptor {

    /**
     * 逻辑删除全局属性名
     */
    private String logicDeleteField;
    /**
     * 逻辑未删除全局值（默认 0、表示未删除）
     */
    private String logicNotDeleteValue;

    private List<String> externalLogicDeleteTables;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 外部表不进行添加逻辑删除
        if(judgeExternalTable(boundSql.getSql())) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        if (CommonUtil.isBlank(this.logicDeleteField)
                && InterceptorIgnoreHelper.willIgnoreDynamicTableName(ms.getId())
                && !ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            return;
        }
        String sql = mpBs.sql();
        if (CommonUtil.isBlank(sql) || sql.contains(this.logicDeleteField)) {
            return;
        }
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            this.handleSql(selectBody);
            mpBs.sql(select.toString());
        } catch (JSQLParserException e) {
            Log.logger.error("Wrong SQL:{},Params:{}", sql, JSON.toJSONString(parameter));
            throw new SQLException(e);
        }
    }

    private void handleSql(SelectBody selectBody) throws JSQLParserException {
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            for (SelectBody body : selectBodyList) {
                this.setWhere((PlainSelect) body);
            }
        }
    }

    private void setWhere(PlainSelect plainSelect) throws JSQLParserException {
        if (plainSelect.getFromItem() instanceof SubSelect) {
            SubSelect select = (SubSelect) plainSelect.getFromItem();
            SelectBody selectBody = select.getSelectBody();
            this.handleSql(selectBody);
            return;
        }
        // 构建子查询 -- 逻辑删除
        Expression deleteExpression = this.getLogicDeleteSql(plainSelect.getFromItem());
        if (deleteExpression == null) {
            return;
        }
        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(deleteExpression);
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), deleteExpression));
        }
        List<Join> joins = plainSelect.getJoins();
        if (CommonUtil.isEmpty(joins)) {
            return;
        }
        for (Join join : joins) {
            if (CommonUtil.isNotEmpty(join.getOnExpressions())) {
                for (Expression onExpression : join.getOnExpressions()) {
                    Expression joinDeleteExpression = this.getLogicDeleteSql(join.getRightItem());
                    if (joinDeleteExpression == null) {
                        continue;
                    }
                    join.setOnExpressions(CommonUtil.ofList(new AndExpression(onExpression, joinDeleteExpression)));
                    break;
                }
            } else {
                if (plainSelect.getWhere() == null) {
                    plainSelect.setWhere(this.getLogicDeleteSql(join.getRightItem()));
                } else {
                    plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), this.getLogicDeleteSql(join.getRightItem())));
                }
            }

        }
    }

    private Expression getLogicDeleteSql(FromItem fromItem) throws JSQLParserException {
        String aliasName;
        Alias fromItemAlias = fromItem.getAlias();
        Table table = (Table) fromItem;
        String originalTableName = table.getName();
        if (PartitionManager.tableNameNotExist(originalTableName)) {
            return null;
        }
        if (fromItemAlias == null) {
            fromItem.setAlias(new Alias(originalTableName));
            aliasName = originalTableName;
        } else {
            aliasName = fromItemAlias.getName();
        }
        return CCJSqlParserUtil.parseCondExpression(CommonUtil.isBlank(aliasName) ? this.logicDeleteField + " = " + this.logicNotDeleteValue : aliasName + "." + this.logicDeleteField + " = " + this.logicNotDeleteValue);
    }

    private boolean judgeExternalTable(String sql) {
        boolean flag = false;
        for(String table : externalLogicDeleteTables) {
            if(sql.indexOf(table) > -1) {
                flag = true;
            }
        }
        return flag;
    }

}
