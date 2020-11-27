package com.labbol.core.check.sign;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import com.labbol.core.check.sign.annotation.SignValidate;
import com.labbol.core.check.sign.bean.RequestMapBean;
import com.labbol.core.check.sign.holder.RequestMapHolder;
import com.labbol.service.exception.CommonException;

/**
 * @ClassName: AbstractSignHandlerInterceptor
 * @Description: 签名过滤器
 * @author dwayne
 * @date 2020年3月20日
 *
 */
public abstract class AbstractSignHandlerInterceptor extends org.yelong.support.spring.mvc.interceptor.AbstractHandlerInterceptor {
	
    private final Logger logger = LoggerFactory.getLogger(AbstractSignHandlerInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			SignValidate signValidate = getSignValidate(handlerMethod);
			logger.info("是否存在注解SignValidate===============" + signValidate);
			// 判断是否需要验证sign
			if(!isValidateSign(request, handlerMethod, signValidate))
				return true;
			RequestMapBean bean = init(request, signValidate.headerParameters(), signValidate.signParamName());
			boolean result = false;
			CommonException commonException = null;
			try {
				result = validateSign(bean.getParameterMap(), bean.getHeaderMap(), bean.getBody(), bean.getSign());
			}catch(CommonException e) {
				commonException = e;
			}
			logger.info("sign验证结果============" + result);
			if(!result)
				invalidSignHandler(request, response, commonException);
			else
				RequestMapHolder.setRequestMapBean(bean);
			return  result;
		}
		return true;
	}
	
	/**
	 * @Description: 获取SignValidate注解
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:56:49 
	 * @param @param hanlderMethod
	 * @param @return 参数 
	 * @return SignValidate 返回类型 
	 * @throws
	 */
	protected SignValidate getSignValidate(HandlerMethod hanlderMethod) {
		Method method = hanlderMethod.getMethod();
		if(method.isAnnotationPresent(SignValidate.class)) {
			return method.getAnnotation(SignValidate.class);
		}
		Class<?> c = hanlderMethod.getBeanType();
		if(c.isAnnotationPresent(SignValidate.class)) {
			return c.getAnnotation(SignValidate.class);
		}
		Class<?> superClass = c.getSuperclass();
		while(true) {
			if(superClass == Object.class) {
				return null;
			}
			if(superClass.isAnnotationPresent(SignValidate.class)) {
				return superClass.getAnnotation(SignValidate.class);
			}
			superClass = superClass.getSuperclass();
		}
	}
	
	/**
	 * @Description: 判断是否sign验证
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:51:29 
	 * @param @param request
	 * @param @param handlerMethod
	 * @param @param signValidate
	 * @param @return 参数 
	 * @return boolean 返回类型 
	 * @throws
	 */
	protected boolean isValidateSign(HttpServletRequest request, HandlerMethod handlerMethod, @org.yelong.core.annotation.Nullable SignValidate signValidate) {
		if(null == signValidate || !signValidate.validate())
			return false;
		return true;
	}
	
	/**
	 * @Description: sign验证
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:52:18 
	 * @param @param parameterMap
	 * @param @param headerMap
	 * @param @param body
	 * @param @param sign
	 * @param @return 参数 
	 * @return boolean 返回类型 
	 * @throws
	 */
	protected abstract boolean validateSign(Map<String, String> parameterMap, Map<String, String> headerMap, String body, String sign);
	
	/**
	 * @Description: 获取请求信息
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:52:40 
	 * @param @param parameterMap
	 * @param @param headerMap
	 * @param @param body
	 * @param @return 参数 
	 * @return RequestMapBean 返回类型 
	 * @throws
	 */
	protected abstract RequestMapBean getRequestMapBean(Map<String, String> parameterMap, Map<String, String> headerMap, String body);
	
	/**
	 * @Description: sign验证处理器
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:58:36 
	 * @param @param request
	 * @param @param response
	 * @param @param commonException
	 * @param @throws Exception 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	protected void invalidSignHandler(HttpServletRequest request, HttpServletResponse response , CommonException commonException) throws Exception {}
	
	/**
	 * @Description: 初始化 
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:53:28 
	 * @param @param request
	 * @param @param headerParameters
	 * @param @param signParamName
	 * @param @return
	 * @param @throws IOException 参数 
	 * @return RequestMapBean 返回类型 
	 * @throws
	 */
	protected RequestMapBean init(HttpServletRequest request, String []headerParameters, String signParamName) throws IOException {
		RequestMapBean bean = new RequestMapBean();
		initHeaderMap(request, headerParameters, bean);
		initParameterMap(request, bean);
		initRequestBody(request, bean);
		initSign(request, signParamName, bean);
		return bean;
	}
	
	/**
	 * @Description: 请求头初始化
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:53:47 
	 * @param @param request
	 * @param @param headerParameters
	 * @param @param bean 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	protected void initHeaderMap(HttpServletRequest request, String []headerParameters, RequestMapBean bean){
		Map<String, String> headerMap = new HashMap<String, String>();
		if(ArrayUtils.isEmpty(headerParameters))
			return;
		for(String headerParameter : headerParameters) {
			headerMap.put(headerParameter, request.getHeader(headerParameter));
		}
		bean.setHeaderMap(headerMap);
	}
	
	/**
	 * @Description: 请求参数初始化
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:54:09 
	 * @param @param request
	 * @param @param bean 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	protected void initParameterMap(HttpServletRequest request, RequestMapBean bean) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		Enumeration<?> parameterNames  = request.getParameterNames();
		while(parameterNames.hasMoreElements()) {  
            String parameterName = (String) parameterNames.nextElement();  
            String parameterValue = request.getParameter(parameterName);  
            if(null != parameterValue)
            	parameterMap.put(parameterName, parameterValue);
        }
		bean.setParameterMap(parameterMap);
	}
	
	/**
	 * @Description: 请求体初始化 
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:54:51 
	 * @param @param request
	 * @param @param bean
	 * @param @throws IOException 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	protected void initRequestBody(HttpServletRequest request, RequestMapBean bean) throws IOException {
		if(StringUtils.isNotBlank(request.getHeader("Content-Type")) && 
				request.getHeader("Content-Type").contains("multipart/form-data"))
			return;
		else
			bean.setBody(org.yelong.support.servlet.wrapper.HttpServletRequestReuseWrapper.readerBodyStr(request));
	}
	
	/**
	 * @Description: sign初始化
	 * @author: dwayne
	 * @date: 2020年11月27日 下午6:55:14 
	 * @param @param request
	 * @param @param signParamName
	 * @param @param bean 参数 
	 * @return void 返回类型 
	 * @throws
	 */
	protected void initSign(HttpServletRequest request, String signParamName, RequestMapBean bean) {
		bean.setSign(request.getHeader(signParamName));
	}
	
}
