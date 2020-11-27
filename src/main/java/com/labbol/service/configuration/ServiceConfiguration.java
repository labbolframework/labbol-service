/**
 * 
 */
package com.labbol.service.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.labbol.core.Labbol;
import com.labbol.core.check.sign.DefaultSignInterceptor;
import com.labbol.core.check.token.DefaultAuthTokenInterceptor;
import com.labbol.core.check.token.UndefinedTokenInterceptor;
import com.labbol.service.exception.ServiceExceptionResolver;

/**
 * @author PengFei
 */
public class ServiceConfiguration {

	@Bean
	public ServiceExceptionResolver serviceExceptionResolver() {
		return new ServiceExceptionResolver();
	}
	
	@Bean
	@ConditionalOnProperty(prefix = Labbol.LABBOL_PROPERTIES_PREFIX, name = "loginMode", havingValue = "sign", matchIfMissing = false)
	@Order(10000)
	public DefaultSignInterceptor defaultSignInterceptor() {
		return new DefaultSignInterceptor();
	}
	
	@Bean
	@ConditionalOnProperty(prefix = Labbol.LABBOL_PROPERTIES_PREFIX, name = "loginMode", havingValue = "token", matchIfMissing = false)
	@Order(10000)
	public DefaultAuthTokenInterceptor defaultAuthTokenInterceptor() {
		return new DefaultAuthTokenInterceptor();
	}
	
	@Bean
	@ConditionalOnProperty(prefix = Labbol.LABBOL_PROPERTIES_PREFIX, name = "loginMode", havingValue = "test", matchIfMissing = false)
	@Order(10000)
	public UndefinedTokenInterceptor undefinedTokenInterceptor() {
		return new UndefinedTokenInterceptor();
	}

}
