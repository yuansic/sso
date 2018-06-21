package com.x.sso.handler;

import java.util.HashMap;
import java.util.Map;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.x.sso.principal.BssCredentials;

import javax.validation.constraints.NotNull;

@Component("bssPrincipalResolver")
public class BssCredentialToPrincipalResolver implements PrincipalResolver {

	@NotNull
	protected PrincipalFactory principalFactory = new DefaultPrincipalFactory();

	public BssCredentialToPrincipalResolver() {}

	@Override
	public final Principal resolve(Credential credential) {
		BssCredentials bssCredentials = (BssCredentials) credential;

		String principalId = bssCredentials.getUsername();

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("tenantId", bssCredentials.getTenantId());
		attributes.put("username", bssCredentials.getUsername());
		attributes.put("userId", bssCredentials.getUserId());
		attributes.put("loginName", bssCredentials.getLoginName());
        attributes.put("mobile", bssCredentials.getMobile());
        attributes.put("email", bssCredentials.getEmail());
        attributes.put("officeId", bssCredentials.getOfficeId());
        attributes.put("companyId", bssCredentials.getCompanyId());
        attributes.put("userType", bssCredentials.getUserType());
        attributes.put("category", bssCredentials.getCategory());
		return this.principalFactory.createPrincipal(principalId , attributes);
	}

	@Override
	public boolean supports(Credential paramCredential) {
		return paramCredential!=null&&BssCredentials.class.isAssignableFrom(paramCredential.getClass());
	}

	@Autowired
	public void setPrincipalFactory(@Qualifier("principalFactory") PrincipalFactory principalFactory) {
		this.principalFactory = principalFactory;
	}

}
