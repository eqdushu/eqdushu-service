package com.eqdushu.server.intercepter;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.eqdushu.server.annotation.NeedAdmin;
import com.eqdushu.server.annotation.NeedReader;
import com.eqdushu.server.annotation.NonAuthorization;
import com.eqdushu.server.domain.auth.TokenModel;
import com.eqdushu.server.domain.user.User;
import com.eqdushu.server.vo.Response;
import com.eqdushu.server.service.auth.ITokenService;
import com.eqdushu.server.service.user.IUserService;
import com.eqdushu.server.utils.AuthUtil;
import com.eqdushu.server.utils.Constants;
import com.eqdushu.server.utils.EnumHolder;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.WebUtil;
import com.eqdushu.server.utils.jackson.JsonUtil;

/**
 * 操作鉴权intercepter
 * @author lzphoenix
 * user.id = userExtra.userId
 *
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ITokenService tokenManager;

	@Autowired
	private IUserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		// 如果方法注明了NonAuthorization，则不需要登录token验证
		if (method.getAnnotation(NonAuthorization.class) != null) {
			return true;
		}

		// 从header中得到token
		String authorization = request.getHeader(Constants.HEADER_TOKEN);
		// 验证token
		TokenModel model = tokenManager.getToken(authorization);
		
		if (tokenManager.checkToken(model)) {
			User user = userService.getById(model.getUserId());
			if (method.getAnnotation(NeedAdmin.class) != null) { // 需要是图书管理员才能执行该操作
				if (user == null || !EnumHolder.UserType.admin.equals(user.getUserType())) {
					WebUtil.outputJsonString(JsonUtil.toJson(new Response(ResponseCode.need_admin)), request, response);
					return false;
				}
			}else if (method.getAnnotation(NeedReader.class) != null) {// 需要用户注册进了公司才能执行该操作,且需管理员审核通过
				if (user == null || !EnumHolder.UserType.reader.equals(user.getUserType())
						|| !EnumHolder.UserStatus.normal.equals(user.getStatus())) {
					WebUtil.outputJsonString(JsonUtil.toJson(new Response(ResponseCode.need_reader)), request, response);
					return false;
				}
			}

			AuthUtil.setUserId(model.getUserId());
			return true;
		} else {
			WebUtil.outputJsonString(
					JsonUtil.toJson(new Response(ResponseCode.unauthorized)),
					request, response);
			return false;
		}
	}
	
	
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		AuthUtil.clear();
	}

}
