/**
 * 
 */
package com.labbol.core.check.token;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.yelong.core.model.service.ModelService;
import org.yelong.support.spring.mvc.token.AbstractTokenHandlerInterceptor;
import org.yelong.support.spring.mvc.token.InvalidTokenException;
import org.yelong.support.spring.mvc.token.TokenValidate;

import com.labbol.core.platform.org.model.Org;
import com.labbol.core.platform.user.model.User;

/**
 * 未定义token认证。 默认添加请求人为系统管理员
 * 
 * @author PengFei
 */
public class UndefinedTokenInterceptor extends AbstractTokenHandlerInterceptor {

	private static final String DEFAULT_TOKEN = "99999999";

	@Resource
	private ModelService modelService;

	@Override
	protected boolean validateToken(String token) throws InvalidTokenException {
		return true;
	}

	@Override
	protected org.yelong.support.spring.mvc.token.RequestUserInfo validTokenHandler(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod hanlderMethod, String token) throws Exception {
		return getDefaultRequestUserInfo();
	}

	@Override
	protected boolean isValidateToken(HttpServletRequest request, HandlerMethod handlerMethod,
			TokenValidate tokenValidate) {
		if (null != tokenValidate) {
			return tokenValidate.validate();
		}
		return true;
	}

	@Override
	protected String getToken(HttpServletRequest request, TokenValidate tokenValidate) {
		return DEFAULT_TOKEN;
	}

	protected org.yelong.support.spring.mvc.token.RequestUserInfo getDefaultRequestUserInfo() {
		RequestUserInfo cocoonRequestUserInfo = new RequestUserInfo(DEFAULT_TOKEN);
		// cocoonRequestUserInfo.setOpRights(modelService.findAll(Module.class).stream().map(Module::getId).collect(Collectors.toList()));
		User user = new User();
		user.setUsername("system");
		user.setRealName("彭飞");
		user.setIsSuper("1");
		cocoonRequestUserInfo.setUser(user);
		Org org = new Org();
		org.setId("9999999999999");
		return cocoonRequestUserInfo;
	}

}
