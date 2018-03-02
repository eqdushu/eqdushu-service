package com.eqdushu.server.mapper.reward;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.eqdushu.server.domain.reward.UserReward;
import java.util.List;

@Repository
public interface UserRewardMapper {
	public Long create(UserReward userReward);
	
	public void update(UserReward userReward);

	public UserReward getById(Long id);
	
	public UserReward getByUserId(Long userId);
	
	public List<UserReward> listByUserIds(@Param("uids")List<Long> uids);
}
