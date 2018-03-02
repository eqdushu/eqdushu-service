package com.eqdushu.server.controller.user;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.eqdushu.server.service.sms.ISmsService;
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.system.AppContext;
import com.eqdushu.server.utils.EnumHolder;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.common.base.Strings;
import com.eqdushu.server.domain.auth.AuthDto;
import com.eqdushu.server.domain.auth.TokenModel;
import com.eqdushu.server.utils.AuthUtil;
import com.eqdushu.server.utils.Constants;
import com.eqdushu.server.annotation.NonAuthorization;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.event.RewardCreateEvent;
import com.eqdushu.server.exception.PasswdException;
import com.eqdushu.server.exception.SignInException;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException.Exc;
import com.eqdushu.server.service.auth.IAuthService;
import com.eqdushu.server.service.auth.ITokenService;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.EnumHolder.SceneType;

@Controller
@RequestMapping("/user/auth")
@LogInfo
@LogException(value = { @Exc(value = Exception.class, stacktrace = false) }, warn = { @Exc({ IllegalArgumentException.class }) })
public class UserController {
	private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IAuthService authService;
	
	@Autowired
	private ITokenService tokenService;

	@Autowired
	private ISmsService iSmsService;

	@Autowired
	private IUserService iUserService;

	@NonAuthorization
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody Response register(
			@RequestBody Map<String, Object> params) {
		String company = MapUtils.getString(params, "company");
		String phone = MapUtils.getString(params, "phone");
		String passwd = MapUtils.getString(params, "passwd");
		String type = MapUtils.getString(params, "type");
		String termType = MapUtils.getString(params, "termTyp");
		String channel = MapUtils.getString(params, "channel");
		String smsCode = MapUtils.getString(params,"smsCode");
		if (Strings.isNullOrEmpty(phone)
				|| Strings.isNullOrEmpty(passwd)
				|| Strings.isNullOrEmpty(smsCode)
				|| (!Constants.TERMINAL_TYPE_ANDROID.equals(termType) && !Constants.TERMINAL_TYPE_IOS
						.equals(termType))) {
			return new Response(ResponseCode.invalid_attribute);
		}
		boolean verify = iSmsService.checkSms(phone,smsCode);
		if(!verify){
			//短信校验错误
			return new Response(ResponseCode.verifysms_error);
		}
		User user = authService.getByTypeAndScene(null, SceneType.phone,phone);
		if (user != null) {
			return new Response(ResponseCode.existed_user);
		}
		User userNew = authService.register(company, phone, passwd, type, termType, channel);
		if(userNew!=null){
			AppContext.instance().getApplicationContext().publishEvent(new RewardCreateEvent(this,userNew));
			return new Response(ResponseCode.success);
		}
		return new Response(ResponseCode.operate_failed);
	}
	
	@NonAuthorization
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Response login(@RequestBody Map<String, Object> params)
			throws RuntimeException, Exception {
		String phone = MapUtils.getString(params, "phone");
		String passwd = MapUtils.getString(params, "passwd");
		//String type = MapUtils.getString(params, "type");
		String deviceId = MapUtils.getString(params, "deviceId");

		String channel = MapUtils.getString(params, "channel");

		if (Strings.isNullOrEmpty(phone) || Strings.isNullOrEmpty(passwd)
				//|| Strings.isNullOrEmpty(type)
				|| passwd.length() < Constants.PASSWORD_LEN_MIN
				|| passwd.length() > Constants.PASSWORD_LEN_MAX) {
			return new Response(ResponseCode.invalid_attribute);
		}
		Response response = null;
		try{
		  AuthDto authDto = authService.login(phone, passwd, null, deviceId, channel);
		  response = new Response(ResponseCode.success, authDto);
		}catch (SignInException se) {
			switch (se.getCode()) {
			case unkonw_account: 
				response = new Response(ResponseCode.unknown_user);
				break;
			case audit_account:
				response = new Response(ResponseCode.user_need_audit);
				break;
			case incorrect_credentials:
				Map<String, Object> retryData = new HashMap<String, Object>();
				retryData.put("limitCount", se.getRetryLimitCount());
				retryData.put("retryCount", se.getRetryCount());
				retryData.put("lockTimeInSeconds", se.getLockTimeInSeconds());
				response = new Response(ResponseCode.incorrect_credentials,
						retryData);
				if (se.getRetryCount() < se.getRetryLimitCount()) {
					response.setRspInf(String.format("密码错误，您今日剩下%d次输入密码机会",
							se.getRetryLimitCount() - se.getRetryCount()));
				} else {
					response.setRspInf(String.format(
							"密码错误，您今日密码已输错%d次，请尝试找回密码或明天再尝试",
							se.getRetryLimitCount()));
				}
				break;
			case abnormal_account:
				response = new Response(ResponseCode.user_need_audit);
				break;
			default:
				response = new Response(ResponseCode.unknown_error);
				break;
			}
		}
		return response;
		
	}
	
	/**
	 * @param params
	 *            oldPass : 旧密码, newPass : 新密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changepass", method = RequestMethod.POST)
	public @ResponseBody Response changePwd(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		String oldPass = MapUtils.getString(params, "oldPass");
		String newPass = MapUtils.getString(params, "newPass");

		if (Strings.isNullOrEmpty(oldPass) || Strings.isNullOrEmpty(newPass)) {
			return new Response(ResponseCode.invalid_attribute);
		}
		Response response = null;
		try {
			authService.changePasswd(AuthUtil.getUserId(), oldPass, newPass);
			response = new Response(ResponseCode.success);
		} catch (PasswdException cpme) {
			response = new Response(ResponseCode.incorrect_password);
		}
		return response;
	}

	@NonAuthorization
	@RequestMapping(value = "/resetpass", method = RequestMethod.POST)
	public @ResponseBody Response resetPwd(
			@RequestBody Map<String, Object> params, HttpServletRequest request) {
		String newPass = MapUtils.getString(params, "newPass");
		String phone = MapUtils.getString(params, "phone");
		String type = MapUtils.getString(params, "type");
		String smsCode = MapUtils.getString(params,"smsCode");

		if (Strings.isNullOrEmpty(newPass) || Strings.isNullOrEmpty(phone)
				|| Strings.isNullOrEmpty(type)|| Strings.isNullOrEmpty(smsCode)) {
			return new Response(ResponseCode.invalid_attribute);
		}
		boolean verify = iSmsService.checkSms(phone,smsCode);
		if(!verify){
			//短信校验错误
			return new Response(ResponseCode.verifysms_error);
		}
		authService.resetPasswd(phone, type, newPass);
		return new Response(ResponseCode.success);
	}

	
	/**
	 * 
	 * @param params
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody Response logout(//从header中获取token，删除token缓存即认为退出登录
			@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		String authentication = request.getHeader(Constants.HEADER_TOKEN);
		TokenModel model = tokenService.getToken(authentication);
		tokenService.deleteToken(model.getUserId());
		return new Response(ResponseCode.success);
	}


	/**
	 * 退出公司
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/outCompany", method = RequestMethod.POST)
	public @ResponseBody Response outCompany(HttpServletRequest request)
			throws Exception {
		//获取用户信息
		Long userId = AuthUtil.getUserId();

		User user = iUserService.getById(userId);
		//判断是否有多个管理员
		if (user != null && EnumHolder.UserType.admin.equals(user.getUserType())) {
			int adminNum = iUserService.getAdminNum(user.getCompanyId());
			if (adminNum < 2) {
				return new Response(ResponseCode.company_admin_lost);
			}
		}
		int res = iUserService.updateCompanyNull(userId);
		if(1!=res){
			return new Response(ResponseCode.operate_failed);
		}
		return new Response(ResponseCode.success);
	}
}
