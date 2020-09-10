/**
 * 
 */
package com.labbol.service.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.yelong.support.spring.mvc.exception.AbstractHandlerExceptionResolverByResponseWay;

/**
 * 异常处理
 * 
 * @author PengFei
 */
public class ServiceExceptionResolver extends AbstractHandlerExceptionResolverByResponseWay {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionResolver.class);

	@Override
	protected String handlerExceptionResponseJson(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		ServiceException ServiceException = null;
		if (ex instanceof ServiceException) {
			ServiceException = ((ServiceException) ex);
		} else {
			ServiceException = new ServiceInernalErrorException(ex);
		}
		// 出现异常后，日志详情默认是异常信息
		// LogRecordUtils.setLogDesc(ServiceException.getMessage());
		// 将异常信息记录到日志中
		LOGGER.error(ex.getMessage(), ex);
		return ServiceException.getErrorResponseJson();
	}

	@Override
	protected ModelAndView handlerExceptionResponseModelAndView(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		return new ModelAndView("500.jsp");
	}

}
