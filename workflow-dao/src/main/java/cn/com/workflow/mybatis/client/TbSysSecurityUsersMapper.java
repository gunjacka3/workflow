package cn.com.workflow.mybatis.client;

import cn.com.workflow.mybatis.model.TbSysSecurityUsers;

public interface TbSysSecurityUsersMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TbSysSecurityUsers record);

    int insertSelective(TbSysSecurityUsers record);

    TbSysSecurityUsers selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(TbSysSecurityUsers record);

    int updateByPrimaryKey(TbSysSecurityUsers record);
}