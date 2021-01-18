/**
 * 
 */
package com.labbol.core.check.token;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.yelong.commons.util.Dates;
import org.yelong.core.model.collector.ModelCollectors;
import org.yelong.support.spring.mvc.token.AbstractTokenHandlerInterceptor;
import org.yelong.support.spring.mvc.token.InvalidTokenException;

import com.labbol.core.check.CurrentLoginUserInfoHolder;
import com.labbol.core.platform.org.model.Org;
import com.labbol.core.platform.token.model.Token;
import com.labbol.core.platform.user.model.User;
import com.labbol.core.platform.user.service.UserRightCommonService;
import com.labbol.core.service.LabbolModelService;
import com.labbol.service.exception.AuthTokenErrorException;

/**
 * @author PengFei
 */
public class DefaultAuthTokenInterceptor extends AbstractTokenHandlerInterceptor {

	@Resource
	private LabbolModelService modelService;

	@Resource
	private UserRightCommonService userRightService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthTokenInterceptor.class);

	@Override
	protected boolean validateToken(String token) throws InvalidTokenException {
		if (StringUtils.isEmpty(token) || !tokenIsValid(token)) {
			// 如果token无效则抛出token无效异常
			throw new AuthTokenErrorException(token);
		} else {
			// 续命
			Token tokenModel = new Token();
			tokenModel.setAuthExpireTime(DateUtils.addMonths(Dates.nowDate(), 60));
			modelService.collect(ModelCollectors.modifyModelBySingleColumnEQ(tokenModel, "authToken", token));
		}
		return true;
	}

	@Override
	protected org.yelong.support.spring.mvc.token.RequestUserInfo validTokenHandler(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod hanlderMethod, String authToken) throws Exception {
		LabbolModelService modelService = getModelService();
		Token sqlTokenModel = new Token();
		sqlTokenModel.setAuthToken(authToken);
		Token token = modelService.findFirstBySqlModel(Token.class, sqlTokenModel);
		User user = modelService.findById(User.class, token.getUserId());
		Org org = modelService.findById(Org.class, user.getOrgId());
		RequestUserInfo requestUserInfo = new RequestUserInfo(authToken);
		requestUserInfo.setOrg(org);
		requestUserInfo.setUser(user);
		requestUserInfo.setOpRights(userRightService.findModuleIds(user.getId()));

		CurrentLoginUserInfoHolder.setCurrentLoginUserInfo(requestUserInfo);
		LOGGER.info(
				"--------------------------------------------------token验证完毕--------------------------------------------------");
		return requestUserInfo;
	}

	@Override
	protected void invalidTokenHandler(HttpServletRequest request, HttpServletResponse response,
			InvalidTokenException invalidTokenException) throws Exception {
		// super.writer(response, "用户令牌认证异常");
		throw invalidTokenException;
	}

	/**
	 * 验证token是否有效
	 * 
	 * @param token token
	 * @return <code>true</code> 有效
	 */
	private boolean tokenIsValid(String token) {
		if (StringUtils.isEmpty(token)) {
			return false;
		}
		Token sqlTokenModel = new Token();
		sqlTokenModel.setAuthToken(token);
		Token tokenModel = modelService.findFirstBySqlModel(Token.class, sqlTokenModel);
		if (null == tokenModel) {
			return false;
		}
		Date authExpireTime = tokenModel.getAuthExpireTime();
		if (null == authExpireTime) {
			return false;
		}
		Long authExpireTimeLong = null;
		authExpireTimeLong = ((Date) authExpireTime).getTime();
		return authExpireTimeLong > new Date().getTime();
	}

	public LabbolModelService getModelService() {
		return this.modelService;
	}

	public void setModelService(LabbolModelService modelService) {
		this.modelService = modelService;
	}

}
