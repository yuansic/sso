package com.x.data.service.busi.impl;

import com.x.base.exception.BusinessException;
import com.x.data.api.user.param.UserQueryRequest;
import com.x.data.constants.AccountExceptCode;
import com.x.data.service.atom.interfaces.ILoginAtomSV;
import com.x.data.service.busi.interfaces.ISysUserValidateSV;
import com.x.data.service.busi.interfaces.IVoValidateSV;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VoValidateSVImpl implements IVoValidateSV {


    @Autowired
    ISysUserValidateSV iSysUserValidateSV;

    @Autowired
    ILoginAtomSV iLoginAtomSV;


    @Override
    public void validateLogin(String username) throws BusinessException {

        iSysUserValidateSV.checkUserName(username);
    }


    @Override
    public void validateQueryAccountBaseInfo(UserQueryRequest accountQueryRequest)
            throws BusinessException {
        if (accountQueryRequest == null) {
            throw new BusinessException(AccountExceptCode.ErrorCode.PARAM_NULL_ERROR, "参数对象为空");
        }
        iSysUserValidateSV.checkUserId(accountQueryRequest.getUserId());
    }


}
