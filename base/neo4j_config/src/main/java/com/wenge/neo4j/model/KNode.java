package com.wenge.neo4j.model;

import lombok.Data;

import java.util.Map;
import java.util.Objects;


@Data
public class KNode {

    /**
     * 属性列表
     */
    private Map<String, Object> properties;

    /**
     * 节点Id
     */
    private String id;

    /**
     * 节点类别
     */
    private String group;

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            //判断是否是同类型的对象进行比较
            if (obj instanceof KNode) {
                KNode node = (KNode) obj;
                if (node.getId().equals(this.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProperties(), getId(), getGroup());
    }
}
