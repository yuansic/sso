package com.x.data.api.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.x.base.exception.BusinessException;
import com.x.base.exception.SystemException;
import com.x.base.vo.ResponseHeader;
import com.x.data.api.user.interfaces.ILoginSV;
import com.x.data.api.user.param.UserLoginResponse;
import com.x.data.constants.AccountConstants.ResultCode;
import com.x.data.dao.mapper.bo.SysUser;
import com.x.data.service.busi.interfaces.ILoginBusiSV;
import com.x.data.service.busi.interfaces.IVoValidateSV;
import com.x.data.util.RegexUtils;
import com.x.sdk.util.BeanUtils;

@Service
@Component
public class LoginSVImpl implements ILoginSV {
	@Autowired
	private ILoginBusiSV iLoginBusiSV;
	@Autowired
	IVoValidateSV iVoValidateSV;

	@Override
	public UserLoginResponse queryUserByUserName(String username) throws BusinessException, SystemException {
		iVoValidateSV.validateLogin(username);
		// 判断用户名是手机还是邮箱
		boolean isEmial = RegexUtils.checkIsEmail(username);
		boolean isPhone = RegexUtils.checkIsPhone(username);
		SysUser account = new SysUser();
		if (isPhone == true) {
			account.setMobile(username);
		} else if (isEmial == true) {
			account.setEmail(username);
		} else {
			account.setLoginName(username);
		}
		SysUser sysUser = iLoginBusiSV.queryByUserName(account);
		// 组织返回对象
		UserLoginResponse response = new UserLoginResponse();
		if (sysUser != null) {
			BeanUtils.copyProperties(response, sysUser);
			response.setUserId(sysUser.getId());
			response.setLoginName(sysUser.getLoginName());
			response.setEmail(sysUser.getEmail());
			response.setMobile(sysUser.getMobile());
			response.setLoginPassword(sysUser.getPassword());
			// add by zhouxh
			response.setLoginFlag(sysUser.getLoginFlag());
			response.setLoginFlag(sysUser.getLoginFlag());
			response.setOfficeId(sysUser.getOfficeId());
			response.setCategory(sysUser.getCategory());
			response.setCompanyId(sysUser.getCompanyId());
			response.setUserType(sysUser.getUserType());
			response.setDelFlag(sysUser.getDelFlag());
			response.setEffectiveDate(sysUser.getEffectiveDate());
			response.setEffectiveDate(sysUser.getEffectiveDate());

			ResponseHeader responseHeaders = new ResponseHeader(true, ResultCode.SUCCESS_CODE, "成功");
			response.setResponseHeader(responseHeaders);
		} else {
			ResponseHeader responseHeaders = new ResponseHeader(false, ResultCode.FAIL_CODE, "用户不存在");
			response.setResponseHeader(responseHeaders);
		}
		return response;
	}

}
