package com.wenge.neo4j.component;

import com.fasterxml.jackson.databind.util.LRUMap;
import com.wenge.neo4j.model.Atlas;
import com.wenge.neo4j.model.KGraph;
import com.wenge.neo4j.model.KLink;
import com.wenge.neo4j.model.KNode;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class Neo4jDriverComponent {

    @org.springframework.beans.factory.annotation.Value("${spring.data.neo4j.uri}")
    private String uri;

    @org.springframework.beans.factory.annotation.Value("${spring.data.neo4j.username}")
    private String username;

    @org.springframework.beans.factory.annotation.Value("${spring.data.neo4j.password}")
    private String password;


    public void executeCommand(String cql) {
        Driver driver = getDriver();
        Session session = driver.session();
        System.out.println(cql);
        session.run(cql);
        session.close();
    }

    public KGraph getSchemaGraph(String cql) {
        Driver driver = getDriver();
        Session session = driver.session();
        System.out.println(cql);
        Result result = session.run(cql);
        KGraph graph = getSchemaGraph(result);
        session.close();
        return graph;
    }



    public KGraph getGraph( String cql) {
        Driver driver = getDriver();
        TransactionConfig transactionConfig = TransactionConfig.builder()
                .withTimeout(Duration.ofSeconds(10))
                .build();
        Result result = null;
        KGraph graph = null;

        try (Session session = driver.session()) {
            System.out.println(cql);
            result = session.run(cql, transactionConfig);
            graph = getMultipleNodes(result);
        } catch (Exception e) {
            throw new RuntimeException("查询时间过长，请检查查询条件是否合理", e);
        }
        return graph;
    }

    private Driver getDriver() {
        Driver driver = Neo4jDriverUtils.neo4jDriver(uri, username, password);
        return driver;
    }





    /**
     * 获取属性值
     *
     * @param result
     * @return
     */
    private List<String> getPropertyValues(Result result) {

        List<String> valueList = new ArrayList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            for (Value value : values) {
                valueList.add(value.toString());
            }
        }
        return valueList;
    }

    /**
     * 获取节点数据
     *
     * @param result
     * @return
     */
    private List<KNode> getSingleNodes(Result result) {
        List<KNode> kNodes = new LinkedList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            for (Value value : values) {
                // 节点
                Node node = value.asNode();
                for (String label : node.labels()) {
                    KNode kNode = new KNode();
                    kNode.setGroup(label);
                    kNode.setId(String.valueOf(node.id()));
                    kNode.setProperties(node.asMap());
                    kNodes.add(kNode);
                }
            }
        }
        return kNodes;
    }

    private List<KLink> getSingleRelations(Result result) {
        List<KLink> links = new LinkedList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            for (Value value : values) {
                // 节点
                if(value instanceof Relationship) {
                    Relationship relationship = value.asRelationship();
                    KLink link = new KLink();
                    link.setId(String.valueOf(relationship.id()));
                    String type = relationship.type();
                    link.setSource(String.valueOf(relationship.startNodeId()));
                    link.setTarget(String.valueOf(relationship.endNodeId()));
                    link.setProperties(relationship.asMap());
                    link.setRelationName(type);
                    if (!links.contains(link)) {
                        links.add(link);
                    }
                }
            }
        }
        return links;
    }

    private KGraph getSchemaGraph(Result result) {
        KGraph graph = new KGraph();

        List<KNode> kNodes = new LinkedList<>();
        List<KLink> kLinks = new LinkedList<>();

        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            ListValue nodes = (ListValue) values.get(0);
            ListValue links = (ListValue) values.get(1);
            // 节点设置
            for (Value value : nodes.values()) {
                Node node = value.asNode();
                for (String label : node.labels()) {
                    KNode kNode = new KNode();
                    kNode.setGroup(label);
                    kNode.setId(String.valueOf(node.id()));
                    if (!kNodes.contains(kNode)) {
                        kNodes.add(kNode);
                    }
                }
            }
            // 关系设置
            for (Value value : links.values()) {
                Relationship relationship = value.asRelationship();
                KLink link = new KLink();
                link.setId(String.valueOf(relationship.id()));
                String type = relationship.type();
                link.setSource(String.valueOf(relationship.startNodeId()));
                link.setTarget(String.valueOf(relationship.endNodeId()));
                link.setRelationName(type);
                link.setProperties(relationship.asMap());
                if (!kLinks.contains(link)) {
                    kLinks.add(link);
                }
            }
        }

        graph.setKNodes(kNodes);
        graph.setKLinks(kLinks);
        return graph;
    }

    /**
     * 获取关系数据
     *
     * @param result
     * @return
     */
    private KGraph getMultipleNodes(Result result) {
        KGraph graph = new KGraph();

        List<KNode> kNodes = new LinkedList<>();
        List<KLink> kLinks = new LinkedList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            // 遍历
            for (Value value : values) {
                // 节点
                if (value instanceof NodeValue) {
                    Node node = value.asNode();
                    for (String label : node.labels()) {
                        KNode kNode = new KNode();
                        kNode.setGroup(label);
                        kNode.setId(String.valueOf(node.id()));
                        kNode.setProperties(node.asMap());
                        if (!kNodes.contains(kNode)) {
                            kNodes.add(kNode);
                        }
                    }
                } else {
                    for (Node node : value.asPath().nodes()) {
                        for (String label : node.labels()) {
                            // 设置
                            KNode kNode = new KNode();
                            kNode.setGroup(label);
                            kNode.setId(String.valueOf(node.id()));
                            kNode.setProperties(node.asMap());
                            if (!kNodes.contains(kNode)) {
                                kNodes.add(kNode);
                            }
                        }
                    }
                    // 边
                    for (Relationship relationship : value.asPath().relationships()) {
                        // 设置
                        KLink link = new KLink();
                        link.setId(String.valueOf(relationship.id()));
                        String type = relationship.type();
                        link.setSource(String.valueOf(relationship.startNodeId()));
                        link.setTarget(String.valueOf(relationship.endNodeId()));
                        link.setRelationName(type);
                        link.setProperties(relationship.asMap());
                        if (!kLinks.contains(link)) {
                            kLinks.add(link);
                        }
                    }
                }
            }
        }

        graph.setKNodes(kNodes);
        graph.setKLinks(kLinks);

        return graph;
    }


    private KGraph setNodes(Result result) {
        KGraph graph = new KGraph();

        List<KNode> kNodes = new LinkedList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            // 遍历
            for (Value value : values) {
                // 节点
                Node node = value.asNode();
                for (String label : node.labels()) {
                    KNode kNode = new KNode();
                    kNode.setGroup(label);
                    kNode.setId(String.valueOf(node.id()));
                    kNode.setProperties(node.asMap());
                    kNodes.add(kNode);
                }
            }
        }
        graph.setKNodes(kNodes);
        return graph;
    }

    private KGraph setNodesAndLinks(Result result) {
        KGraph graph = new KGraph();

        List<KNode> kNodes = new LinkedList<>();
        List<KLink> kLinks = new LinkedList<>();
        while (result.hasNext()) {
            Record record = result.next();
            List<Value> values = record.values();
            // 遍历
            for (Value value : values) {
                // 节点
                for (Node node : value.asPath().nodes()) {
                    for (String label : node.labels()) {
                        // 设置
                        KNode kNode = new KNode();
                        kNode.setGroup(label);
                        kNode.setId(String.valueOf(node.id()));
                        kNode.setProperties(node.asMap());
                        if (!kNodes.contains(kNode)) {
                            kNodes.add(kNode);
                        }
                    }
                }
                // 边
                for (Relationship relationship : value.asPath().relationships()) {
                    // 设置
                    KLink link = new KLink();
                    link.setId(String.valueOf(relationship.id()));
                    String type = relationship.type();
                    link.setSource(String.valueOf(relationship.startNodeId()));
                    link.setTarget(String.valueOf(relationship.endNodeId()));
                    link.setRelationName(type);
                    if (!kLinks.contains(link)) {
                        kLinks.add(link);
                    }
                }
            }
        }
        graph.setKNodes(kNodes);
        graph.setKLinks(kLinks);

        return graph;
    }
}
