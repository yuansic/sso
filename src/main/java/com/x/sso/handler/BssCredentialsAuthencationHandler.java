package com.x.sso.handler;

import java.security.GeneralSecurityException;
import java.util.Date;

import javax.annotation.Resource;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.handler.NoOpPrincipalNameTransformer;
import org.jasig.cas.authentication.handler.PasswordEncoder;
import org.jasig.cas.authentication.handler.PlainTextPasswordEncoder;
import org.jasig.cas.authentication.handler.PrincipalNameTransformer;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.authentication.support.PasswordPolicyConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.x.base.exception.SystemException;
import com.x.data.api.user.param.UserLoginResponse;
import com.x.sdk.util.Md5Encoder;
import com.x.sdk.util.StringUtil;
import com.x.sso.constants.SSOConstants;
import com.x.sso.exception.AccountNameNotExistException;
import com.x.sso.exception.AccountNotAllowLoginException;
import com.x.sso.exception.EmailNotExistException;
import com.x.sso.exception.PasswordErrorException;
import com.x.sso.exception.PasswordIsNullException;
import com.x.sso.exception.PhoneNotExistException;
import com.x.sso.exception.SystemErrorException;
import com.x.sso.exception.UsernameIsNullException;
import com.x.sso.principal.BssCredentials;
import com.x.sso.service.LoadAccountService;
import com.x.sso.util.RegexUtils;

@Component("bssAuthenticationHandler")
public final class BssCredentialsAuthencationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

	@Resource
	private LoadAccountService loadAccountService;
	@NotNull
	private PasswordEncoder passwordEncoder;

	@NotNull
	private PrincipalNameTransformer principalNameTransformer;
	private PasswordPolicyConfiguration passwordPolicyConfiguration;

	public LoadAccountService getLoadAccountService() {
		return loadAccountService;
	}

	public void setLoadAccountService(LoadAccountService loadAccountService) {
		this.loadAccountService = loadAccountService;
	}

	public BssCredentialsAuthencationHandler() {
		this.passwordEncoder = new PlainTextPasswordEncoder();
		this.principalNameTransformer = new NoOpPrincipalNameTransformer();
	}

	@Override
	public boolean supports(Credential credentials) {
		return credentials != null && (BssCredentials.class.isAssignableFrom(credentials.getClass()));
	}

	@Override
	protected HandlerResult doAuthentication(final Credential credentials)
			throws GeneralSecurityException, PreventedException {
		logger.debug("开始认证用户凭证credentials");
		if (credentials == null) {
			logger.info("用户凭证credentials为空");
			throw new LoginException("Credentials is null");
		}
		BssCredentials bssCredentials = (BssCredentials) credentials;
		final String username = bssCredentials.getUsername();
		final String pwdFromPage = bssCredentials.getPassword();
		// 用户名非空校验
		if (!StringUtils.hasText(username)) {
			logger.error("请输入手机号码或邮箱地址");
			throw new UsernameIsNullException();
		}
		// 密码非空校验
		if (!StringUtils.hasText(pwdFromPage)) {
			logger.error("密码为空！");
			throw new PasswordIsNullException();
		}

		UserLoginResponse user = null;
		try {
			long dubboStart = System.currentTimeMillis();
			logger.error("====开始执行doAuthentication中的loadAccountService.loadAccount服务，当前时间戳：" + dubboStart);
			user = loadAccountService.loadAccount(bssCredentials);
			long dubboEnd = System.currentTimeMillis();
			logger.error("====完成执行doAuthentication中的loadAccountService.loadAccount服务，当前时间戳：" + dubboEnd + ",用时:"
					+ (dubboEnd - dubboStart) + "毫秒");

			if (SSOConstants.ACCOUNT_LOGIN_FLAG.equals(user.getLoginFlag())) {
				// 账号不允许登录
				logger.error("账号不允许登录");
				throw new AccountNotAllowLoginException();
			}
			if (user == null || StringUtil.isBlank(user.getUserId())) {
				if (RegexUtils.checkIsPhone(bssCredentials.getUsername())) {
					logger.error("手机号码未注册");
					throw new PhoneNotExistException();
				} else if (RegexUtils.checkIsEmail(bssCredentials.getUsername())) {
					logger.error("邮箱未绑定");
					throw new EmailNotExistException();
				} else if (SSOConstants.ACCOUNT_LOGIN_FLAG.equals(user.getLoginFlag())) {
					// 账号不允许登录
					logger.error("账号不允许登录");
					throw new AccountNotAllowLoginException();
				} else if (SSOConstants.ACCOUNT_DEL_FLAG.equals(user.getDelFlag())) {
					// 账号已删除
					logger.error("账号已删除");
					throw new AccountNameNotExistException();
				} else {
					logger.error("账号未注册");
					throw new AccountNameNotExistException();
				}
			}

			String dbPwd = user.getLoginPassword();
			logger.info("【dbPwd】=" + dbPwd);
			logger.info("【pwdFromPage】=" + pwdFromPage);
			String encryPwdFromPage = Md5Encoder.encodePassword(pwdFromPage);
			if (!encryPwdFromPage.equals(dbPwd)) {
				// 密码不对
				logger.error("密码错误！");
				throw new PasswordErrorException();
			}
			Date currentDate = new Date();
			Date acitveDate = user.getEffectiveDate();
			Date inactiveDate = user.getExpiryDate();
			if (acitveDate != null && currentDate.before(acitveDate)) {
				throw new CredentialException("账号未生效");
			}
			if (inactiveDate != null && inactiveDate.before(currentDate)) {
				throw new CredentialException("账号已失效");
			}
			bssCredentials.setOfficeId(user.getOfficeId());
			bssCredentials.setTenantId(user.getTenantId());
			bssCredentials.setUserId(user.getUserId());
			bssCredentials.setMobile(user.getMobile());
			bssCredentials.setEmail(user.getEmail());
			bssCredentials.setLoginName(user.getLoginName());
			bssCredentials.setCompanyId(user.getCompanyId());
			bssCredentials.setCategory(user.getCategory());
			bssCredentials.setUserType(user.getUserType());

		} catch (SystemException e) {
			logger.error("调用查询账户服务（Dubbo）失败", e);
			throw new CredentialException("系统错误");
		} catch (AccountNotAllowLoginException e) {
			logger.error("该用户已被冻结", e);
			throw new AccountNotAllowLoginException();
		} catch (Exception e) {
			logger.error("系统异常", e);
			throw new SystemErrorException();
		}
		logger.info("用户 [" + username + "] 认证成功。");
		return this.createHandlerResult(bssCredentials, this.principalFactory.createPrincipal(username), null);
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public PrincipalNameTransformer getPrincipalNameTransformer() {
		return principalNameTransformer;
	}

	public void setPrincipalNameTransformer(PrincipalNameTransformer principalNameTransformer) {
		this.principalNameTransformer = principalNameTransformer;
	}

	public PasswordPolicyConfiguration getPasswordPolicyConfiguration() {
		return passwordPolicyConfiguration;
	}

	public void setPasswordPolicyConfiguration(PasswordPolicyConfiguration passwordPolicyConfiguration) {
		this.passwordPolicyConfiguration = passwordPolicyConfiguration;
	}

}
