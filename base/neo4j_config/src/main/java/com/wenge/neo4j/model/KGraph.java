package com.wenge.neo4j.model;

import lombok.Data;

import java.util.List;

@Data
public class KGraph {


    /**
     * 节点列表
     */
    private List<KNode> KNodes;
    /**
     * 关系列表
     */
    private List<KLink> KLinks;
}
