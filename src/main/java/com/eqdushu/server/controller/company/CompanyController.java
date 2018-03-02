package com.eqdushu.server.controller.company;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.eqdushu.server.annotation.NeedAdmin;
import org.apache.commons.collections.MapUtils;
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
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.utils.ResponseCode;

@Controller
@RequestMapping("/company")
@LogInfo
@LogException(value = { @Exc(value = Exception.class, stacktrace = false) }, warn = {
		@Exc({ IllegalArgumentException.class }) })
public class CompanyController {

	@Autowired
	private IUserService userService;

	/**
	 * 
	 * @param params companyId 用户所属公司ID
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/member/list", method = RequestMethod.POST)
	public @ResponseBody Response listReaders(@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		Long companyId = MapUtils.getLong(params, "companyId");

		if (companyId == null) {//company id不能为空
			return new Response(ResponseCode.invalid_attribute);
		}

		return new Response(ResponseCode.success, userService.listByCompanyId(companyId));
	}

	/**
	 * 管理员移除用户
	 * @param params usrId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@NeedAdmin
	@RequestMapping(value = "/member/remove", method = RequestMethod.POST)
	public @ResponseBody Response removeReader(@RequestBody Map<String, String> params, HttpServletRequest request)
			throws Exception {
		Long removeUserId = MapUtils.getLong(params, "removeUserId");

		if (removeUserId == null) {//removeUserId id不能为空
			return new Response(ResponseCode.invalid_attribute);
		}
		int result = userService.updateCompanyNull(removeUserId);
		if(1 != result){
			return new Response(ResponseCode.operate_failed);
		}
		return new Response(ResponseCode.success);
	}

}
