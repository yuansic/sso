package com.x.data.service.busi.interfaces;

import com.x.base.exception.BusinessException;
import com.x.data.dao.mapper.bo.SysUser;

public interface ILoginBusiSV {
    SysUser queryByUserName(SysUser user) throws BusinessException;
    
}
