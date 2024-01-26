package com.wenge.neo4j.model;

import lombok.Data;

import java.util.Map;
import java.util.Objects;

@Data
public class KLink {

    /**
     * Id
     */
    private String id;
    /**
     * 起始节点
     */
    private String source;
    /**
     * 终止节点
     */
    private String target;
    /**
     * 关系名称
     */
    private String relationName;
    /**
     * 属性列表
     */
    private Map<String, Object> properties;

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            //判断是否是同类型的对象进行比较
            if (obj instanceof KLink) {
                KLink link = (KLink) obj;
                if (link.getId().equals(this.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSource(), getTarget(), getRelationName(), getProperties());
    }
}
