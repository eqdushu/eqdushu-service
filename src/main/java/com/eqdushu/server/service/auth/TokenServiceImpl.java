package com.eqdushu.server.service.auth;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.eqdushu.server.domain.auth.TokenModel;
import com.eqdushu.server.utils.CipherUtil;
import com.eqdushu.server.utils.Constants;

/**
 * 登录token管理类
 * 
 * @author lzphoenix
 *
 */
@Service
public class TokenServiceImpl implements ITokenService {

	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setRedis(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		// 泛型设置成Long后必须更改对应的序列化方案
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 创建token，token字符串构成为userId_token
	 */
	public TokenModel createToken(Long userId) {
		String token = UUID.randomUUID().toString().replace("-", "");
		TokenModel model = new TokenModel(userId, token);
		redisTemplate.boundValueOps(Constants.HEADER_TOKEN + ":" + userId).set(
				token, Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
		return model;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean checkToken(TokenModel model) {//登录是成功的
		if (model == null) {
			return false;
		}

		String token = (String) redisTemplate.boundValueOps(
				Constants.HEADER_TOKEN + ":" + model.getUserId()).get();

		if (token == null || !token.equals(model.getToken())) {
			return false;
		}

		// 如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
		redisTemplate.boundValueOps(
				Constants.HEADER_TOKEN + ":" + model.getUserId()).expire(
				Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteToken(Long userId) {
		redisTemplate.delete(Constants.HEADER_TOKEN + ":" + userId);
	}

	@Override
	public TokenModel getToken(String authentication) throws Exception {
		if (Strings.isNullOrEmpty(authentication)) {
			return null;
		}
		String[] param = authentication.split("_");
		if (param.length != 2) {
			return null;
		}

		String decryptUserId = CipherUtil.base64DecodeAndDecrypt(param[0],
				Constants.ENCRYPT_KEY);// CipherUtil.AESDecryptFromBase64(param[0],
		// Constants.ENCRYPT_KEY);

		// 使用userId和源token拼接成的token
		long userId = Long.parseLong(decryptUserId);
		String token = param[1];
		return new TokenModel(userId, token);
	}

	@Override
	public Long extractUserId(String xAuthToken) throws Exception {
		TokenModel model = getToken(xAuthToken);
		if (model != null) {
			return model.getUserId();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	/**
	 * 返回传入userId列表中所有已登录用户的userId
	 */
	public List<String> loginUserIds(List<Long> userIds){
		List<String> userKeys = userIds.stream().map(o->Constants.HEADER_TOKEN + ":"+o).collect(Collectors.toList());
		
		return redisTemplate.opsForValue().multiGet(userKeys);
	}

}
