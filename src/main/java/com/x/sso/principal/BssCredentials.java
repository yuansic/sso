package com.x.sso.principal;

import org.jasig.cas.authentication.RememberMeUsernamePasswordCredential;

/**
 * BssCredentials
 * 
 */
public class BssCredentials extends RememberMeUsernamePasswordCredential {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7331124263252464003L;
	/**
	 * 租户ID
	 */
	private String tenantId;
	/**
	 * 账号ID
	 */
	private String userId;
	/**
	 * 账号名称
	 */
	private String loginName;
	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 邮件
	 */
	private String email;

	/**
	 * 归属公司
	 */
	private String companyId;

	/**
	 * 归属部门
	 */
	private String officeId;

	/**
	 * 用户类型
	 */
	private String userType;

	/**
	 * 部门
	 */
	private String category;
	/**
	 * 验证码
	 */
	private String captchaCode;

	/**
	 * sessionId
	 */
	private String sessionId;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCaptchaCode() {
		return captchaCode;
	}

	public void setCaptchaCode(String captchaCode) {
		this.captchaCode = captchaCode;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
