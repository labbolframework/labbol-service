/**
 * 
 */
package com.labbol.core.check.token;

import com.labbol.core.Labbol;
import com.labbol.core.check.CurrentLoginUserInfo;
import com.labbol.core.platform.role.model.Role;

/**
 * @author PengFei
 */
public class RequestUserInfo extends CurrentLoginUserInfo
		implements org.yelong.support.spring.mvc.token.RequestUserInfo {

	private static final long serialVersionUID = 1654953816498125967L;

	@Deprecated
	private Role role;

	public RequestUserInfo(String token) {
		this.setAttribute(Labbol.X_AUTH_TOKEN, token);
	}

	public String getUserName() {
		return getUser() == null ? null : getUser().getUsername();
	}

	public String getOrgId() {
		return getOrg() == null ? null : getOrg().getId();
	}

	public String getOrgName() {
		return getOrg() == null ? null : getOrg().getOrgName();
	}

	public String getOrgNo() {
		return getOrg() == null ? null : getOrg().getOrgNo();
	}

	public String getUserRealName() {
		return getUser() == null ? null : getUser().getRealName();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String getToken() {
		return (String) getAttribute("X-Auth-Token");
	}

	@Override
	public Object get(String key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getKeys() {
		throw new UnsupportedOperationException();
	}

}
