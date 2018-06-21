package com.x.data.service.busi.impl;

import com.x.base.exception.BusinessException;
import com.x.data.dao.mapper.bo.SysUser;
import com.x.data.service.atom.interfaces.ILoginAtomSV;
import com.x.data.service.busi.interfaces.ILoginBusiSV;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class LoginBusiSVImpl implements ILoginBusiSV {
    @Autowired
    ILoginAtomSV iLoginAtomSV;

    @Override
    public SysUser queryByUserName(SysUser user) throws BusinessException {

        return iLoginAtomSV.queryByUserName(user);

    }

}
