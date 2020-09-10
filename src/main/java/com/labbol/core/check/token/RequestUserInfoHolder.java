/**
 * 
 */
package com.labbol.core.check.token;

/**
 * @author PengFei
 */
public class RequestUserInfoHolder {

	/**
	 * 获取当前的请求用户信息<br/>
	 * 
	 * @return 请求用户信息
	 */
	public static RequestUserInfo currentRequestUserInfo() {
		return (RequestUserInfo) org.yelong.support.spring.mvc.token.RequestUserInfoHolder.currentRequestUserInfo();
	}

}
