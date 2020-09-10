package com.labbol.service.configuration.mybatis;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.labbol.core.Labbol;

@Configuration
@ConditionalOnProperty(prefix = Labbol.LABBOL_PROPERTIES_PREFIX,
name = "mycat",
havingValue = "true",
matchIfMissing = false)
@ConditionalOnBean(SqlSessionFactory.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MyBatisMycatAutoConfiguration {
	
	@Autowired
	private List<SqlSessionFactory> sqlSessionFactoryList;

	@PostConstruct
	public void addMyBaticParamInterceptor() {
		MycatInterceptorAutoConfiguration interceptor = new MycatInterceptorAutoConfiguration();
		sqlSessionFactoryList.forEach( x -> x.getConfiguration().addInterceptor(interceptor) );
	}

}
