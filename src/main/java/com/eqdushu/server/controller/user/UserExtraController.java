package com.eqdushu.server.controller.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.base.Strings;
import com.eqdushu.server.annotation.NeedAdmin;
import com.eqdushu.server.domain.company.Company;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.domain.user.UserExtra;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException.Exc;
import com.eqdushu.server.service.company.ICompanyService;
import com.eqdushu.server.service.user.IUserExtraService;
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.utils.AuthUtil;
import com.eqdushu.server.utils.EnumHolder;
import com.eqdushu.server.utils.EnumHolder.UserType;
import com.eqdushu.server.utils.ResponseCode;

@Controller
@RequestMapping("/user/extra")
@LogInfo
@LogException(value = { @Exc(value = Exception.class, stacktrace = false) }, warn = {
		@Exc({ IllegalArgumentException.class }) })
public class UserExtraController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserExtraService userExtraService;

	@Autowired
	private ICompanyService companyService;

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response updateExtra(@RequestBody Map<String, Object> params) {

		String deviceId = MapUtils.getString(params, "deviceId");

		if (Strings.isNullOrEmpty(deviceId)) {
			return new Response(ResponseCode.invalid_attribute);
		}

		UserExtra userExtra = userExtraService.getByUserId(AuthUtil.getUserId());
		if (userExtra != null) {
			if (!deviceId.equals(userExtra.getDeviceId())) { // 更新设备ID
				userExtra.setDeviceId(deviceId);
				userExtraService.update(userExtra);
			}
		}

		return new Response(ResponseCode.success);
	}

	/**
	 * 
	 * @param params
	 *            type 用户类型
	 * @param params
	 *            companyName 公司名称，管理员必传字段
	 * @param params
	 *            companyCode 公司预设编码，用户必传且必须唯一
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/set-type", method = RequestMethod.POST)
	public @ResponseBody Response setType(// 从header中获取token，删除token缓存即认为退出登录
			@RequestBody Map<String, String> params, HttpServletRequest request) throws Exception {
		String type = MapUtils.getString(params, "type");
		String companyName = MapUtils.getString(params, "companyName");
		String companyCode = MapUtils.getString(params, "companyCode");
		String userName = MapUtils.getString(params, "userName");
		if (Strings.isNullOrEmpty(type) || Strings.isNullOrEmpty(companyCode)
				|| (EnumHolder.UserType.admin.name().equals(type) && Strings.isNullOrEmpty(companyName))) {
			return new Response(ResponseCode.invalid_attribute);
		}
		if (EnumHolder.UserType.admin.name().equals(type)) {
			Long userId = AuthUtil.getUserId();
			UserExtra userExtra = userExtraService.getByUserId(userId);
			if (userExtra != null && userExtra.getCompanyId() != null) {
				return new Response(ResponseCode.duplicate_company_create);
			}

			if (companyService.getByCode(companyCode) != null) {
				return new Response(ResponseCode.duplicate_company_code);
			}

			Company company = new Company();
			company.setName(companyName);
			company.setCode(companyCode);
			companyService.create(company);
			if (company.getId() != null) {
				updateUserInfo(EnumHolder.UserType.admin, userId, company.getId(), companyName, userName);
			}
		} else if (EnumHolder.UserType.reader.name().equals(type)) {
			Company company = companyService.getByCode(companyCode);
			if (company == null) {
				return new Response(ResponseCode.company_code_error);
			}
			Long companyId = company.getId();
			if (companyId == null) {
				return new Response(ResponseCode.invalid_attribute);
			}
			Long userId = AuthUtil.getUserId();
			updateUserInfo(EnumHolder.UserType.reader, userId, companyId, companyName, userName);
		}

		return new Response(ResponseCode.success);
	}

	/**
	 * 
	 * @param params
	 *            userId 用户userId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@NeedAdmin
	@RequestMapping(value = "/reader/audit", method = RequestMethod.POST)
	public @ResponseBody Response auditReader(@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		Long userId = MapUtils.getLong(params, "userId");

		if (userId == null) {
			return new Response(ResponseCode.invalid_attribute);
		}

		User user = userService.getById(userId);
		if (user == null) {
			return new Response(ResponseCode.unknown_user);
		}
		user.setStatus(EnumHolder.UserStatus.normal);
		userService.update(user);
		return new Response(ResponseCode.success);
	}

	private void updateUserInfo(UserType userType, Long userId, Long companyId, String companyName, String userName) {
		User user = new User();
		user.setId(userId);
		user.setCompanyId(companyId);
		user.setCompany(companyName);
		user.setUserType(userType);
		/*if (UserType.admin.equals(userType)) {
			user.setStatus(EnumHolder.UserStatus.normal);
		} else if (UserType.reader.equals(userType)) {
			user.setStatus(EnumHolder.UserStatus.audit);
		}*/
		user.setStatus(EnumHolder.UserStatus.normal);//前期开放加入公司，不做管理员审核操作
		user.setUserName(userName);
		userService.update(user);
		UserExtra userExtra = new UserExtra();
		userExtra.setUserId(userId);
		userExtra.setCompanyId(companyId);
		userExtra.setCompany(companyName);
		userExtra.setUserType(userType.name());
		userExtra.setUserName(userName);
		userExtraService.update(userExtra);
	}
}
