package com.eqdushu.server.service.auth;

import com.eqdushu.server.domain.auth.AuthDto;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.exception.RegisterException;
import com.eqdushu.server.utils.EnumHolder.SceneType;

public interface IAuthService {
	public User register(String company,String phone, String passwd, String type, String termType, String channel) throws RegisterException;
	
	public User getByTypeAndScene(String type,SceneType sceneType,String account);
	
	public AuthDto login(String phone, String passwd, String type,String deviceId, String channel) throws Exception;
	
    public void changePasswd(Long userId, String oldPass, String newPass) throws RuntimeException;
	
	public void resetPasswd(String phone, String type, String passwd);
}
