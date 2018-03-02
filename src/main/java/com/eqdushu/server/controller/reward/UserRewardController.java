package com.eqdushu.server.controller.reward;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.eqdushu.server.domain.reward.UserReward;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException.Exc;
import com.eqdushu.server.service.reward.IUserRewardService;
import com.eqdushu.server.utils.AuthUtil;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.blockchain.AssetValue;
import com.eqdushu.server.utils.blockchain.NeoReturn;
import com.eqdushu.server.utils.blockchain.NeoUtil;

@Controller
@RequestMapping("/user/reward")
@LogInfo
@LogException(value = { @Exc(value = Exception.class, stacktrace = false) }, warn = {
		@Exc({ IllegalArgumentException.class }) })
public class UserRewardController {

	private final static Logger LOG = LoggerFactory.getLogger(UserRewardController.class);

	@Autowired
	private IUserRewardService userRewardService;

	/**
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public @ResponseBody Response getReward(@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		Long userId = AuthUtil.getUserId();

		UserReward userReward = userRewardService.getByUserId(userId);

		if (userReward == null) {
			userReward = new UserReward();
			userReward.setUserId(userId);
			Map<String, Object> tmpMap = NeoUtil.getNewAddress();
			if (tmpMap == null || MapUtils.getString(tmpMap, "error") != null) {
				LOG.warn("response error:", MapUtils.getString(tmpMap, "error"));
				return new Response(ResponseCode.operate_failed);
			}
			String address = MapUtils.getString(tmpMap, "result");
			if (StringUtils.isBlank(address)) {
				LOG.warn("address is blank");
				return new Response(ResponseCode.operate_failed);
			}
			userReward.setType("gas");
			userReward.setAddress(address);
			userReward.setAsset(0.0);
			userReward.setCreateTime(new Date());
			userRewardService.create(userReward);
		}
		String[] params_ = new String[1];
		params_[0] = userReward.getAddress();
		NeoReturn neoReturn = NeoUtil.getAccountState(params_);
		if (neoReturn == null || neoReturn.getResult() == null) {
			LOG.warn("response error.");
			return new Response(ResponseCode.operate_failed);
		}

		if (!CollectionUtils.isEmpty(neoReturn.getResult().getBalances())) {
			for (AssetValue av : neoReturn.getResult().getBalances()) {
				if (NeoUtil.neoAsset.equals(av.getAsset())) {
					Double v = Double.valueOf(av.getValue());
					if (v > userReward.getAsset()) {// 查出来后如果大于则更新下数据资产asset
						                            // 默认转账会成功，所以每次都显示最大的
						userReward.setAsset(v);
						try {
							userRewardService.update(userReward);
						} catch (Exception e) {
							LOG.warn("update user reward failed: ", e);
						}
					}
				}
			}
		}

		return new Response(ResponseCode.success, userReward);
	}

	@RequestMapping(value = "/send-random", method = RequestMethod.POST)
	public @ResponseBody Response sendRewardRandom(@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		Long userId = AuthUtil.getUserId();
		UserReward userReward = userRewardService.getByUserId(userId);

		if (userReward == null) {
			userReward = new UserReward();
			userReward.setUserId(userId);
			Map<String, Object> tmpMap = NeoUtil.getNewAddress();
			if (tmpMap == null || MapUtils.getString(tmpMap, "error") != null) {
				LOG.warn("response error:", MapUtils.getString(tmpMap, "error"));
				return new Response(ResponseCode.operate_failed);
			}
			String address = MapUtils.getString(tmpMap, "result");
			if (StringUtils.isBlank(address)) {
				LOG.warn("address is blank");
				return new Response(ResponseCode.operate_failed);
			}
			userReward.setType("gas");
			userReward.setAddress(address);
			userReward.setAsset(0.0);
			userReward.setCreateTime(new Date());
			userRewardService.create(userReward);
		}

		BigDecimal b = new BigDecimal(Math.random() / 10);
		b.add(new BigDecimal(0.01));// 保证数值>=0.01
		Double neoNum = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		String[] params_ = new String[4];
		params_[0] = NeoUtil.neoAsset;
		params_[1] = NeoUtil.neoMainAddress;
		params_[2] = userReward.getAddress();
		params_[3] = String.valueOf(neoNum);
		NeoUtil.sendFrom(params_);

		userReward.setAsset(b.add(new BigDecimal(userReward.getAsset())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		
		try {
			userRewardService.update(userReward);
		} catch (Exception e) {
			LOG.warn("update user reward failed when send-random: ", e);
		}
		
		return new Response(ResponseCode.success,userReward);
	}

}
