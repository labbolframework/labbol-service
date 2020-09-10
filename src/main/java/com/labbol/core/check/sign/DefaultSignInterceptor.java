package com.labbol.core.check.sign;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yelong.core.model.service.SqlModelService;

import com.labbol.core.check.sign.bean.RequestMapBean;
import com.labbol.core.check.sign.constants.Mycat;
import com.labbol.core.check.sign.holder.MycatHolder;
import com.labbol.core.check.sign.holder.SchemaHolder;
import com.labbol.core.platform.mycat.model.TenantSchema;
import com.labbol.core.platform.sign.model.Sign;
import com.labbol.core.utils.SignSupportUtils;
import com.labbol.service.exception.CommonException;
import com.labbol.service.exception.CommonExceptionEnum;

public class DefaultSignInterceptor extends  AbstractSignHandlerInterceptor {
	
    private static final Logger logger = LoggerFactory.getLogger(DefaultSignInterceptor.class);

    @Resource
	private SqlModelService modelService;
	
	@Override
	protected boolean validateSign(Map<String, String> parameterMap, Map<String, String> headerMap, String body,
			String sign) throws CommonException {
		if(headerMap.isEmpty() || !headerMap.containsKey("appKey") || StringUtils.isBlank(headerMap.get("appKey")))
			throw new CommonException(CommonExceptionEnum.MISS_PARAMETER_ERROR, "appkey is empty");
		if(!headerMap.containsKey("timestamp") || StringUtils.isBlank(headerMap.get("timestamp")))
			throw new CommonException(CommonExceptionEnum.MISS_PARAMETER_ERROR, "timestamp is empty");
		if(StringUtils.isBlank(sign))
			throw new CommonException(CommonExceptionEnum.MISS_PARAMETER_ERROR, "sign is empty");
		String appkey = headerMap.get("appKey");
		long timestamp = Long.valueOf(headerMap.get("timestamp"));
		long timenow = System.currentTimeMillis() - 5*60*1000;
		if(timenow > timestamp)
			throw new CommonException(CommonExceptionEnum.TIME_NOT_MATCH);
		MycatHolder.setIsmycat(Mycat.NO.getCode());
		SchemaHolder.setSchema(null);
		Sign signSql = new Sign();
		signSql.setAppKey(appkey);
		Sign signBean = getModelService().findFirstBySqlModel(Sign.class, signSql);
		if(null == signBean)
			throw new CommonException(CommonExceptionEnum.INVALID_APPKEY);
		if(StringUtils.isBlank(signBean.getAppSecret()))
			throw new CommonException(CommonExceptionEnum.INVALID_APPKEY);
		if(!headerMap.isEmpty() && headerMap.containsKey("schemaTag") && StringUtils.isNotBlank(headerMap.get("schemaTag"))) {
			TenantSchema schemaSql = new TenantSchema();
			schemaSql.setEncryptSchema(headerMap.get("schemaTag"));
			TenantSchema schemaBean = getModelService().findFirstBySqlModel(TenantSchema.class, schemaSql);
			if(null != schemaBean && StringUtils.isNotBlank(schemaBean.getSchemaname()))
				SchemaHolder.setSchema(schemaBean.getSchemaname());
		}
		MycatHolder.setIsmycat(Mycat.YES.getCode());
		String generateSign = null;
		try {
			generateSign = SignSupportUtils.generateSign(parameterMap, headerMap, body, signBean.getAppSecret());
		} catch (Exception e) {
			throw new CommonException(CommonExceptionEnum.SERVICE_SIGN_ERROR, "sign生成失败");
		}
		logger.info("sign======================" + sign);
		logger.info("generateSign======================" + generateSign);
		if(StringUtils.isBlank(sign) || StringUtils.isBlank(generateSign))
			throw new CommonException(CommonExceptionEnum.INVALID_SIGN);
		if(!sign.equals(generateSign))
			throw new CommonException(CommonExceptionEnum.INVALID_SIGN);
		return true;
	}

	@Override
	protected RequestMapBean getRequestMapBean(Map<String, String> parameterMap, Map<String, String> headerMap, String body) {
		RequestMapBean bean = new RequestMapBean();
		bean.setParameterMap(parameterMap);
		bean.setHeaderMap(headerMap);
		bean.setBody(body);
		return bean;
	}
	
	@Override
	protected void invalidSignHandler(HttpServletRequest request, HttpServletResponse response, 
			CommonException commonException) throws Exception {
		throw commonException;
	}

	public SqlModelService getModelService() {
		return modelService;
	}

	public void setModelService(SqlModelService modelService) {
		this.modelService = modelService;
	}

}
