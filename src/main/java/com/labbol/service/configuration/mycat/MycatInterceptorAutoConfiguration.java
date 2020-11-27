package com.labbol.service.configuration.mycat;

import java.lang.reflect.Field;
import java.sql.Connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yelong.support.orm.mybaits.interceptor.AbstractInterceptor;

import com.labbol.core.check.sign.constants.Mycat;
import com.labbol.core.check.sign.holder.MycatHolder;
import com.labbol.core.check.sign.holder.SchemaHolder;

@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MycatInterceptorAutoConfiguration extends AbstractInterceptor{
	
    private static final Logger logger = LoggerFactory.getLogger(MycatInterceptorAutoConfiguration.class);
	
    @Override
	public Object intercept(Invocation invocation) throws Throwable {
    	logger.info("进入mycat拦截器...");
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject
                .forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                    new DefaultReflectorFactory());
        /**
	          *先拦截到RoutingStatementHandler
	          *里面有个StatementHandler类型的delegate变量
	          *其实现类是BaseStatementHandler
	          *然后就到BaseStatementHandler的成员变量mappedStatement
         */
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // id为执行的mapper方法的全路径名，如com.uv.dao.UserMapper.insertUser
        String id = mappedStatement.getId();
        System.out.println(id);
        // sql语句类型 insert、delete、update、select
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();
        logger.info("SQL语句的类型(insert、delete、update、select):" + sqlCommandType);
        // 获取到原始sql语句
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        logger.info("初始SQL语句:" + sql);
        String mycatSql = null;
        if(Mycat.NO.getCode().equals(MycatHolder.getIsmycat()))
        	mycatSql = sql;
    	if(StringUtils.isBlank(SchemaHolder.getSchema()))
    		mycatSql = sql;
    	else
    		mycatSql = "/*!mycat:schema = " + SchemaHolder.getSchema() + " */" + sql;
    	logger.info("封装后的执行语句：" +  mycatSql);
        // 通过反射修改sql语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, StringUtils.isNotBlank(mycatSql) ? mycatSql : sql);
        return invocation.proceed();
	}

}
