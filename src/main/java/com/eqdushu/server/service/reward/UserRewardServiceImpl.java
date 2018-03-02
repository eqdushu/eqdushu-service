package com.eqdushu.server.service.reward;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eqdushu.server.domain.reward.UserReward;
import com.eqdushu.server.mapper.reward.UserRewardMapper;

@Service
public class UserRewardServiceImpl implements IUserRewardService {
	
	@Autowired
	private UserRewardMapper userRewardMapper;

	@Override
	public UserReward create(UserReward userReward) {
		userRewardMapper.create(userReward);
		return userReward;
	}

	@Override
	public UserReward getByUserId(Long userId) {
		return userRewardMapper.getByUserId(userId);
	}

	@Override
	public void update(UserReward userReward) {
		userReward.setUpdateTime(new Date());
		userRewardMapper.update(userReward);
	}

}
