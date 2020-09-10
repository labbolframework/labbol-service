/**
 * 
 */
package com.labbol.service.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.labbol.core.Labbol;
import com.labbol.service.exception.ServiceExceptionResolver;

/**
 * @author PengFei
 */
@Configuration
@ConditionalOnProperty(prefix = Labbol.LABBOL_PROPERTIES_PREFIX, name = "devMode", havingValue = "service", matchIfMissing = false)
public class ServiceConfiguration {

	@Bean
	public ServiceExceptionResolver serviceExceptionResolver() {
		return new ServiceExceptionResolver();
	}

}
