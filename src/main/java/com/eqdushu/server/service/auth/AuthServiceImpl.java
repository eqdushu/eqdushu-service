package com.eqdushu.server.service.auth;

import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.eqdushu.server.domain.auth.AuthDto;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.exception.PasswdException;
import com.eqdushu.server.exception.RegisterException;
import com.eqdushu.server.exception.SignInException;
import com.eqdushu.server.service.user.IUserExtraService;
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.utils.CipherUtil;
import com.eqdushu.server.utils.Constants;
import com.eqdushu.server.utils.EnumHolder.SceneType;
import com.eqdushu.server.utils.EnumHolder.UserStatus;
import com.eqdushu.server.utils.EnumHolder.UserType;
import com.eqdushu.server.domain.auth.TokenModel;
import com.eqdushu.server.domain.user.UserExtra;

@Service
public class AuthServiceImpl implements IAuthService {
	
	@Autowired
	private RedisTemplate<String,Integer> redisTemplate;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserExtraService userExtraService;
	
	@Autowired
	private ITokenService tokenService;

	@Override
	public User register(String company, String phone, String passwd, String type, String termType, String channel) throws RegisterException {
		if(StringUtils.isBlank(phone)){
			throw new RegisterException(RegisterException.Code.empty_account);
		}
		
		User user = new User();
		user.setPhone(phone);
		user.setPassword(passwd);
		user.setTermType(termType);
		user.setChannel(channel);
		
		user.setUserType(UserType.guest);
		user.setStatus(UserStatus.normal);
		
		user = userService.create(user);
		
		if(user!=null && user.getId()!=null){
			userExtraService.createExtra(company, user.getId(), phone, UserType.guest.name(), termType, channel);
		}
		return user;
	}

	@Override
	public User getByTypeAndScene(String type, SceneType sceneType,String account) {
		return userService.getByTypeAndScene(type, sceneType,account);
	}
	
	@Override
	public AuthDto login(String phone, String passwd, String type, String deviceId, String channel) throws Exception{
		User user = signIn(phone,passwd,type);
		return getAuthInfo(phone, type, deviceId, channel, user);
	}
	
	@Override
	public void changePasswd(Long userId, String oldPass, String newPass) throws RuntimeException {
		User user = userService.getById(userId);
		if(user==null || Boolean.TRUE.equals(user.getDeleted())){
			throw new PasswdException(PasswdException.Code.account_notexsit);
		}
		if(checkPassowrd(user,oldPass)){
			changePassword(user,newPass);
			clearLoginRecordCache(user.getName(),user.getUserType().name());
		}else{
			throw new PasswdException(PasswdException.Code.passwd_check_failed);
		}
	}

	@Override
	public void resetPasswd(String phone, String type, String passwd) {
		User user = userService.getByTypeAndScene(type, SceneType.phone,phone);
		if(user==null || Boolean.TRUE.equals(user.getDeleted())){
			throw new PasswdException(PasswdException.Code.account_notexsit);
		}
		changePassword(user,passwd);
		clearLoginRecordCache(user.getName(),user.getUserType().name());
	}

	private AuthDto getAuthInfo(String phone, String type, String deviceId, String channel,User user) throws Exception {
		UserExtra userExtra = userExtraService.getByUserId(user.getId());
		AuthDto authDto = null;
		if(userExtra!=null){//之前登录过
			authDto = createAuthDto(userExtra);
			if (StringUtils.isNotBlank(deviceId) && !deviceId.equals(userExtra.getDeviceId())) { // 更新设备ID
				userExtra.setDeviceId(deviceId);
				userExtraService.update(userExtra);
			}
			authDto.setPhone(user.getPhone());
			return authDto;
		}else{//刷新登录信息
			userExtra = new UserExtra();
			userExtra.setUserId(user.getId());
			userExtra.setPhone(phone);
			userExtra.setUserType(user.getUserType().name());
			userExtra.setTermType(user.getTermType());
			userExtra.setChannel(channel);
			userExtra.setDeviceId(deviceId);
			userExtra.setCompanyId(user.getCompanyId());
			userExtra.setCompany(user.getCompany());
			userExtra = userExtraService.create(userExtra);
			authDto = createAuthDto(userExtra);
			authDto.setPhone(user.getPhone());
			return authDto;
		}
	}
	
	private AuthDto createAuthDto(UserExtra userExtra) throws Exception{
		AuthDto authDto = new AuthDto();
		authDto.setUserId(userExtra.getUserId());
		authDto.setUserType(userExtra.getUserType());
		authDto.setUuid(userExtra.getUuid());
		authDto.setCompanyId(userExtra.getCompanyId());
		authDto.setCompany(userExtra.getCompany());
		TokenModel tokenModel = tokenService.createToken(userExtra.getUserId());
		if (tokenModel != null) {
			authDto.setxAuthToken(tokenModel.buildToken());
		}
		return authDto;
	}
	
	private User signIn(String phone, String passwd, String type)
			throws SignInException{
		
		User user = userService.getByTypeAndScene(type, SceneType.phone,phone);
		
		if(user==null||Boolean.TRUE.equals(user.getDeleted())){//账号不存在或已被删除
			throw new SignInException(SignInException.Code.unkonw_account);
		}
		/*if(UserType.reader.equals(user.getUserType()) && UserStatus.audit.equals(user.getStatus())){//读者账号需管理员审核
			throw new SignInException(SignInException.Code.audit_account);
		}*/
		if(UserStatus.abnormal.equals(user.getStatus())){//用户状态可能被禁用
			throw new SignInException(SignInException.Code.abnormal_account);
		}
		
		validate(user, passwd);
		
		return user;
	}
	
	private void validate(User user, String passwd) throws SignInException{
        String username = user.getName();
        String userType = user.getUserType().name();
        
        Integer retryCount = redisTemplate.opsForValue().get(Constants.LOGIN_RETRY_PRE + username + ":" + userType);
        
        retryCount = (retryCount == null) ? 0: retryCount.intValue();

        if (retryCount >= Constants.maxRetryCount) {
           throw new SignInException(SignInException.Code.incorrect_credentials,Constants.maxRetryCount,retryCount,Constants.lockTime);
        }

        if (!matches(user, passwd)) {
            redisTemplate.opsForValue().set(Constants.LOGIN_RETRY_PRE + username+ ":" + userType, ++retryCount,Constants.lockTime,TimeUnit.SECONDS);
            throw new SignInException(SignInException.Code.incorrect_credentials,Constants.maxRetryCount,retryCount,Constants.lockTime);
        } else {
            clearLoginRecordCache(username,userType);
        }
    }
	
	private boolean matches(User user, String passwd) {
        return user.getPassword().equals(CipherUtil.hash(user.getName()+passwd+user.getSalt()));
    }
	
	private void clearLoginRecordCache(String username,String userType) {
    	redisTemplate.delete(Constants.LOGIN_RETRY_PRE + username + ":" + userType);
    }

	private boolean checkPassowrd(User user,String passwd){
	    	String password = CipherUtil.hash(user.getName() + passwd + user.getSalt());
	    	return password.equals(user.getPassword());
	}
	
	public User changePassword(User user, String newPassword) {
        user.randomSalt();
        user.setPassword(CipherUtil.hash(user.getName() + newPassword + user.getSalt()));
        return userService.update(user);
    }
}
