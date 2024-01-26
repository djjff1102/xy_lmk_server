package com.wenge.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author CY
 * @since 2023/1/3 16:41
 */
@Getter
@Setter
public class AuthorityDTO implements Serializable {

    private static final long serialVersionUID = 7713247872749578960L;
//    id,name,code,type,routingAddress

    private Long id;

    private String name;

    private String code;

    private int type;

    private String routingAddress;
}
