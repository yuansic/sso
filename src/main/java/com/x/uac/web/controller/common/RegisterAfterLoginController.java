package com.x.uac.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.*;
import org.jasig.cas.ticket.AbstractTicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.x.sso.principal.BssCredentials;
import com.x.uac.web.constants.Constants;
import com.x.uac.web.model.login.LoginUser;
import com.x.uac.web.util.CacheUtil;

/**
 * 注册后自动登录
 *
 * Date: 2016年3月23日 <br>
 * Copyright (c) 2016 company.com <br>
 * 
 * @author wangyongxin
 */
@Component("registerLoginController")
public class RegisterAfterLoginController extends AbstractController {

	@Qualifier("defaultAuthenticationSystemSupport")
	private AuthenticationSystemSupport authenticationSystemSupport = new DefaultAuthenticationSystemSupport();

	@Autowired
	@Qualifier("centralAuthenticationService")
	private CentralAuthenticationService centralAuthenticationService;

	@Autowired
	@Qualifier("ticketGrantingTicketCookieGenerator")
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView signinView = new ModelAndView();
		// 从cache处理
		String uuid = request.getParameter(Constants.UUID.KEY_NAME);
		LoginUser loginUser = (LoginUser) CacheUtil.getValue(uuid, Constants.LoginConstant.CACHE_NAMESPACE, LoginUser.class);
		// username 和password从cache里取
		if(loginUser != null){
			String username = loginUser.getUserName();
			String password = loginUser.getPassword();
			bindTicketGrantingTicket(username, password, request, response);
			CacheUtil.deletCache(uuid, Constants.LoginConstant.CACHE_NAMESPACE);
		}
		String viewName = getSignInView(request);
		signinView.setViewName(viewName);
		return signinView;
	}

	/**
	 * Invoke generate validate Tickets and add the TGT to cookie.
	 * 
	 * @param loginName
	 *            the user login name.
	 * @param loginPassword
	 *            the user login password.
	 * @param request
	 *            the HttpServletRequest object.
	 * @param response
	 *            the HttpServletResponse object.
	 */
	protected void bindTicketGrantingTicket(String loginName, String loginPassword, HttpServletRequest request, HttpServletResponse response) {
			BssCredentials credentials = new BssCredentials();
			credentials.setUsername(loginName);
			credentials.setPassword(loginPassword);
			AuthenticationContextBuilder builder = new DefaultAuthenticationContextBuilder(this.authenticationSystemSupport.getPrincipalElectionStrategy());
			builder.collect(credentials);
		TicketGrantingTicket ticketGrantingTicket = null;
		try {
			ticketGrantingTicket = centralAuthenticationService.createTicketGrantingTicket(builder.build());
		} catch (AuthenticationException e) {
			logger.error("Validate the login name " + loginName + " failure, can't bind the TGT!", e);
		} catch (AbstractTicketException e) {
			logger.error("createTicketGrantingTicket has exception.", e);
		}
		ticketGrantingTicketCookieGenerator.addCookie(request, response, ticketGrantingTicket.getId());
	}

	/**
	 * Get the signIn view URL.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 * @return redirect URL
	 */
	protected String getSignInView(HttpServletRequest request) {
		String service = ServletRequestUtils.getStringParameter(request, "service", "");
		return ("redirect:login" + (service.length() > 0 ? "?service=" + service : ""));
	}

	public CentralAuthenticationService getCentralAuthenticationService() {
		return centralAuthenticationService;
	}

	public void setCentralAuthenticationService(CentralAuthenticationService centralAuthenticationService) {
		this.centralAuthenticationService = centralAuthenticationService;
	}

	public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
		return ticketGrantingTicketCookieGenerator;
	}

	public void setTicketGrantingTicketCookieGenerator(CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

}