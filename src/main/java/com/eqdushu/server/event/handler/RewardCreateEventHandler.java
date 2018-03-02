package com.eqdushu.server.event.handler;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.eqdushu.server.domain.reward.UserReward;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.event.RewardCreateEvent;
import com.eqdushu.server.service.reward.IUserRewardService;
import com.eqdushu.server.system.DistributiveEventMulticaster;
import com.eqdushu.server.utils.blockchain.NeoUtil;

@Component
@DistributiveEventMulticaster.AsyncListener
public class RewardCreateEventHandler implements ApplicationListener<RewardCreateEvent> {
	
	private final static Logger LOG = LoggerFactory.getLogger(RewardCreateEventHandler.class);
	
	@Autowired
	private IUserRewardService userRewardService;

	@Override
	public void onApplicationEvent(RewardCreateEvent event) {
		
		User user = event.getUser();
		if(user==null || user.getId()==null){
			return;
		}
		
		UserReward userReward = new UserReward();
		
		userReward.setUserId(user.getId());
		Map<String, Object> tmpMap = NeoUtil.getNewAddress();
		if(tmpMap==null || MapUtils.getString(tmpMap, "error")!=null){
			LOG.warn("response error:",MapUtils.getString(tmpMap, "error"));
			return;
		}
		String address = MapUtils.getString(tmpMap, "result");
		if(StringUtils.isBlank(address)){
			LOG.warn("address is blank");
			return;
		}
		userReward.setType("gas");
		userReward.setAddress(address);
		userReward.setAsset(0.0);
		userReward.setCreateTime(new Date());
		
	    UserReward res = userRewardService.create(userReward);
	    System.out.print(res);
	}

}
