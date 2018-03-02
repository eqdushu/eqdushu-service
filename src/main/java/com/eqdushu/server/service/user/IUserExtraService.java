package com.eqdushu.server.service.user;

import com.eqdushu.server.domain.user.UserExtra;
import java.util.List;

public interface IUserExtraService {

	public UserExtra create(UserExtra userExtra);
	
	public UserExtra createExtra(String company, Long userId, String phone, String userType, String termType, String channel);

	public UserExtra getByUserId(Long userId);
	
	public UserExtra getByUuid(String uuid);

	public void update(UserExtra userExtra);

	public List<UserExtra> listByUserIds(List<Long> userIds);
	
	public List<UserExtra> listUserExtraByChannel(String channel,String startTime,String endTime,Integer offset,Integer pageSize);


}
