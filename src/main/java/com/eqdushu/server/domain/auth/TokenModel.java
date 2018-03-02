package com.eqdushu.server.domain.auth;

import com.eqdushu.server.utils.CipherUtil;
import com.eqdushu.server.utils.Constants;

/**
 * 访问接口的凭证token
 * 
 * @author lzphoenix
 * user.id = userExtra.userId
 *
 */
public class TokenModel {
	private Long userId;
	// 随机生成的UUID
	private String token;

	public TokenModel() {
	}

	public TokenModel(Long userId, String token) {
		this.userId = userId;
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String buildToken() throws Exception {
		if (userId == null || token == null) {
			return "";
		}

		return CipherUtil.encryptAndBase64Encode(userId+"", Constants.ENCRYPT_KEY) + "_" + token;
	}
}
