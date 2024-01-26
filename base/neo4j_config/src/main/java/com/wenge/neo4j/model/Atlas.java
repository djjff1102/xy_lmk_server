package com.wenge.neo4j.model;

import lombok.Data;

import java.io.Serializable;

/**
 * (Atlas)实体类
 *
 * @author makejava
 * @since 2020-03-11 11:16:11
 */
@Data
public class Atlas implements Serializable {
    private static final long serialVersionUID = -22380604355593406L;

    
    private String host;
    
    private String port;
    
    private String username;
    
    private String password;
    
    private String databaseName;

}