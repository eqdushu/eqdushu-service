package com.eqdushu.server.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.eqdushu.server.domain.user.UserExtra;
import java.util.List;

@Repository
public interface UserExtraMapper {
	public void create(UserExtra userExtra);

	public UserExtra getByUserId(Long userId);
	
	public UserExtra getByUuid(String uuid);

	public void update(UserExtra userExtra);

	public List<UserExtra> listByUserIds(@Param("userIds")List<Long> userIds);
	
	public List<UserExtra> listUserExtraByChannel(@Param("channel")String channel,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);
}
