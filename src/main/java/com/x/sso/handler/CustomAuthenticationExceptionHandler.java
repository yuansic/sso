package com.x.sso.handler;

import org.jasig.cas.authentication.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;

import com.x.sso.exception.*;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CustomAuthenticationExceptionHandler{
	
    private static final String UNKNOWN = "UNKNOWN";
    private static final String DEFAULT_MESSAGE_BUNDLE_PREFIX = "authenticationFailure.";
    private static final List<Class<? extends Exception>> DEFAULT_ERROR_LIST =
            new ArrayList<>();

	@NotNull
	private List<Class<? extends Exception>> errors = DEFAULT_ERROR_LIST;
	private String messageBundlePrefix = DEFAULT_MESSAGE_BUNDLE_PREFIX;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationExceptionHandler.class);
    
    static {
        DEFAULT_ERROR_LIST.add(javax.security.auth.login.AccountLockedException.class);
        DEFAULT_ERROR_LIST.add(javax.security.auth.login.FailedLoginException.class);
        DEFAULT_ERROR_LIST.add(javax.security.auth.login.CredentialExpiredException.class);
        DEFAULT_ERROR_LIST.add(javax.security.auth.login.AccountNotFoundException.class);
        DEFAULT_ERROR_LIST.add(org.jasig.cas.authentication.AccountDisabledException.class);
        DEFAULT_ERROR_LIST.add(org.jasig.cas.authentication.InvalidLoginLocationException.class);
        DEFAULT_ERROR_LIST.add(org.jasig.cas.authentication.AccountPasswordMustChangeException.class);
        DEFAULT_ERROR_LIST.add(org.jasig.cas.authentication.InvalidLoginTimeException.class);
        DEFAULT_ERROR_LIST.add(javax.security.auth.login.CredentialException.class);
        DEFAULT_ERROR_LIST.add(UsernameIsNullException.class);
        DEFAULT_ERROR_LIST.add(SystemBusyException.class);
        DEFAULT_ERROR_LIST.add(PasswordIsNullException.class);
        DEFAULT_ERROR_LIST.add(TenantIdIsNullException.class);
        
        DEFAULT_ERROR_LIST.add(AccountNameNotExistException.class);
        DEFAULT_ERROR_LIST.add(EmailNotExistException.class);
        DEFAULT_ERROR_LIST.add(PasswordErrorException.class);
        DEFAULT_ERROR_LIST.add(PhoneNotExistException.class);
        DEFAULT_ERROR_LIST.add(SystemErrorException.class);
        DEFAULT_ERROR_LIST.add(UsernameNotExistException.class);
        
        DEFAULT_ERROR_LIST.add(CaptchaOutTimeException.class);
        DEFAULT_ERROR_LIST.add(CaptchaErrorException.class);
        DEFAULT_ERROR_LIST.add(CaptchaIsNullException.class);
        
        
        DEFAULT_ERROR_LIST.add(AccountNotAllowLoginException.class);

        
    }

    public void setErrors(final List<Class<? extends Exception>> errors) {
        this.errors = errors;
    }

    public final List<Class<? extends Exception>> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }
    
    public void setMessageBundlePrefix(final String prefix) {
        this.messageBundlePrefix = prefix;
    }

    public String handle(final AuthenticationException e, final MessageContext messageContext) {
        if (e != null) {
            final MessageBuilder builder = new MessageBuilder();
            for (final Class<? extends Exception> kind : this.errors) {
                for (final Class<? extends Exception> handlerError : e.getHandlerErrors().values()) {
                    if (handlerError != null && handlerError.equals(kind)) {
                        final String handlerErrorName = handlerError.getSimpleName();
                        final String messageCode = this.messageBundlePrefix + handlerErrorName;
                        messageContext.addMessage(builder.error().code(messageCode).build());
                        return handlerErrorName;
                    }
                }

            }
        }
        final String messageCode = this.messageBundlePrefix + UNKNOWN;
        LOGGER.trace("Unable to translate handler errors of the authentication exception {}. Returning {} by default...", e, messageCode);
        messageContext.addMessage(new MessageBuilder().error().code(messageCode).build());
        return UNKNOWN;
    }
	
}
