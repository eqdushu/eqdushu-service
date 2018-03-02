package com.eqdushu.server.service.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.eqdushu.server.domain.user.UserExtra;
import com.eqdushu.server.mapper.user.UserExtraMapper;

@Service
@CacheConfig(cacheNames = "userExtra")
public class UserExtraServiceImpl implements IUserExtraService{

	//private final Logger LOG = LoggerFactory.getLogger(UserExtraServiceImpl.class);

	@Autowired
	private UserExtraMapper userExtraMapper;

	@Override
	public UserExtra create(UserExtra userExtra) {
		Date createDate = new Date();
		userExtra.setCreateTime(createDate);
		if(StringUtils.isBlank(userExtra.getUuid())){
			String uuid = UUID.randomUUID().toString().replace("-", "")+"_"+createDate.getTime();
			userExtra.setUuid(uuid);
		}
		userExtraMapper.create(userExtra);
		return userExtra;
	}

	@Override
	@Cacheable(key = "'userId:'+#userId", unless = "#result == null")
	public UserExtra getByUserId(Long userId) {
		return userExtraMapper.getByUserId(userId);
	}

	@Override
	@Cacheable(key = "'accid:'+#uuid", unless = "#result == null")
	public UserExtra getByUuid(String uuid) {
		return userExtraMapper.getByUuid(uuid);
	}

	@Override
	@Caching(evict = { 
			@CacheEvict(key = "'userId:'+#userExtra.userId"),
			@CacheEvict(key = "'uuid:'+#userExtra.uuid")})
	public void update(UserExtra userExtra) {
		userExtra.setUpdateTime(new Date());
		userExtraMapper.update(userExtra);
	}

	@Override
	public List<UserExtra> listByUserIds(List<Long> userIds) {
		return userExtraMapper.listByUserIds(userIds);
	}

	@Override
	public List<UserExtra> listUserExtraByChannel(String channel, String startTime, String endTime, Integer offset,
			Integer pageSize) {
		return userExtraMapper.listUserExtraByChannel(channel, startTime, endTime, offset, pageSize);
	}

	@Override
	public UserExtra createExtra(String company, Long userId, String phone, String userType, String termType, String channel) {
		UserExtra userExtra = new UserExtra();
		Date createDate = new Date();
		userExtra.setCreateTime(createDate);
		userExtra.setCompany(company);
		userExtra.setUserId(userId); // 用户中心id
		userExtra.setPhone(phone);
		userExtra.setUserType(userType);
		userExtra.setTermType(termType);
		userExtra.setChannel(channel);
		
		if(StringUtils.isBlank(userExtra.getUuid())){
			String uuid = UUID.randomUUID().toString().replace("-", "")+"_"+createDate.getTime();
			userExtra.setUuid(uuid);
		}
		userExtraMapper.create(userExtra);
		return userExtra;
	}

}
