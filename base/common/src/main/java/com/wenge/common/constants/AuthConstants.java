package com.wenge.common.constants;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wenge.common.util.CommonUtil;
import com.wenge.dto.user.UserDTO;

/**
 * @author HAO灏 2020/8/7 09:54
 */
public class AuthConstants {


	/**
	 * JWT用户名
	 */
	public final static String JWT_USERNAME = "username";
	/**
	 * JWT用户id
	 */
	public final static String JWT_USER_ID = "userId";
	/**
	 * JWT租户Id
	 */
	public final static String JWT_TENANT_ID = "tenantId";

	public final static String JWT_DEFAULT_TOKEN_NAME = "Authorization";

	/**
	 * JWT Token默认密钥
	 */
	public final static String JWT_DEFAULT_SECRET = "quesoar";

	public final static String JWT_DEFAULT_ISSUER = "quesoar";
	public final static String JWT_DEFAULT_AUDIENCE = "quesoar";
	public final static String JWT_DEFAULT_SUBJECT = "quesoar";

	/**
	 * JWT 默认过期时间，3600L，单位秒
	 */
	public final static long JWT_DEFAULT_EXPIRE_SECOND = 3600L;

	/**
	 * 登陆用户token信息key
	 */
	public final static String LOGIN_TOKEN = "login:token:%s";

	/**
	 * 登陆用户信息key
	 */
	public final static String LOGIN_USER = "login:user_id:%s";

	/**
	 * 登陆用户盐值信息key
	 */
	public final static String LOGIN_SALT = "login:salt:%s";

	/**
	 * 登陆用户username token
	 */
	public final static String LOGIN_USER_TOKEN = "login:user:token:%s:%s";
	/**
	 * JWT刷新新token响应状态码，
	 * Redis中不存在，但jwt未过期，不生成新的token，返回461状态码
	 */
	public final static int JWT_INVALID_TOKEN_CODE = 461;

	/**
	 * JWT刷新新token响应状态码
	 */
	public final static int JWT_REFRESH_TOKEN_CODE = 460;
	/**
	 * 验证码
	 */
	public final static String VERIFY_CODE = "verify.code:%s";

	private final static TransmittableThreadLocal<UserDTO> CURRENT_USER = new TransmittableThreadLocal<>();
	public final static TransmittableThreadLocal<Long> CURRENT_TENANT_ID = TransmittableThreadLocal.withInitial(() -> 0L);

	public static UserDTO getCurrentUser() {
		return CURRENT_USER.get();
	}
	public static void setCurrentUser(UserDTO userDTO) {
		CURRENT_USER.set(userDTO);
	}

	public static void remove() {
		CURRENT_USER.remove();
		CURRENT_TENANT_ID.remove();
	}

	public static boolean checkAuthority(String code) {
		UserDTO userDTO = getCurrentUser();
		return CommonUtil.isNotEmpty(userDTO.getAuthorityCode()) && userDTO.getAuthorityCode().contains(code);
	}
}
