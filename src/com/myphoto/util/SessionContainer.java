package com.myphoto.util;

public class SessionContainer {
	
	private String toUrl;//转向的URL(如未登陆时请求的URL，成功登陆后就转向该URL)
	
	private LoginUser loginUser;

	public String getToUrl() {
		return toUrl;
	}

	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}
	
	
	
}
