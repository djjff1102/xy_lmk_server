package com.wenge.neo4j.component;


import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

/**
 * @author wangyinqiang
 */
public class Neo4jDriverUtils {

    public static Driver neo4jDriver(String neo4jUrl, String neo4jName, String neo4jPassword) {
        Driver driver = GraphDatabase.driver(neo4jUrl, AuthTokens.basic(neo4jName, neo4jPassword));
        return driver;
    }
}
