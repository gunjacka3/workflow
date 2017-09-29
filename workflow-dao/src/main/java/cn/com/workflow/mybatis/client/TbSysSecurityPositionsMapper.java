package cn.com.workflow.mybatis.client;

import cn.com.workflow.mybatis.model.TbSysSecurityPositions;

public interface TbSysSecurityPositionsMapper {
    int deleteByPrimaryKey(String positionId);

    int insert(TbSysSecurityPositions record);

    int insertSelective(TbSysSecurityPositions record);

    TbSysSecurityPositions selectByPrimaryKey(String positionId);

    int updateByPrimaryKeySelective(TbSysSecurityPositions record);

    int updateByPrimaryKey(TbSysSecurityPositions record);
}