package com.eqdushu.server.service.reward;

import com.eqdushu.server.domain.reward.UserReward;

public interface IUserRewardService {
	UserReward create(UserReward userReward);
	UserReward getByUserId(Long userId);
	void update(UserReward userReward);
}
