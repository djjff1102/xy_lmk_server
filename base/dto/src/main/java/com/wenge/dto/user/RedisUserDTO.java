package com.wenge.dto.user;


import com.wenge.dto.info.ClientInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedisUserDTO extends UserDTO {
	private static final long serialVersionUID = -3942617573388767340L;
	private ClientInfo clientInfo;
}
