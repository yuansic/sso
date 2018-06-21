package com.x.data.api.user.impl;

import com.x.base.exception.BusinessException;
import com.x.base.exception.SystemException;
import com.x.base.vo.ResponseHeader;
import com.x.data.api.user.interfaces.IUserQuerySV;
import com.x.data.api.user.param.UserQueryRequest;
import com.x.data.api.user.param.UserQueryResponse;
import com.x.data.constants.AccountConstants.ResultCode;
import com.x.data.dao.mapper.bo.SysUser;
import com.x.data.service.busi.interfaces.IUserBusiSV;
import com.x.data.service.busi.interfaces.IVoValidateSV;
import com.x.sdk.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

@Service
@Component
public class UserQuerySVImpl implements IUserQuerySV {

	@Autowired
	IUserBusiSV iUserBusiSV;
	@Autowired
	IVoValidateSV iVoValidateSV;

	@Override
	public UserQueryResponse queryBaseInfo(UserQueryRequest accountQueryRequest) throws BusinessException,SystemException {
		// 入参检查
		iVoValidateSV.validateQueryAccountBaseInfo(accountQueryRequest);
		// 查询数据
		String userId = accountQueryRequest.getUserId();
		SysUser sysUser = iUserBusiSV.queryByUserId(userId);
		// 整理返回对象
		UserQueryResponse response = new UserQueryResponse();
		if (sysUser != null) {
			BeanUtils.copyProperties(response, sysUser);
			response.setUserId(sysUser.getId());
			response.setLoginName(sysUser.getLoginName());
			response.setEmail(sysUser.getEmail());
			response.setMobile(sysUser.getMobile());
		}
		ResponseHeader responseHeader = new ResponseHeader(true, ResultCode.SUCCESS_CODE, "数据查询成功");
		response.setResponseHeader(responseHeader);
		return response;
	}

	
}
