package com.eqdushu.server.service.auth;

import java.util.List;

import com.eqdushu.server.domain.auth.TokenModel;

public interface ITokenService {
	public TokenModel createToken(Long userId);
	public TokenModel getToken(String authentication) throws Exception;
	public boolean checkToken(TokenModel model);
	public void deleteToken(Long userId);
	
	public Long extractUserId(String xAuthToken) throws Exception;
	public List<String> loginUserIds(List<Long> userIds);
}
