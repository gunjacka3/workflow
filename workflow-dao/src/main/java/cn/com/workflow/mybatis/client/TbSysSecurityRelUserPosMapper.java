package cn.com.workflow.mybatis.client;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.workflow.mybatis.model.TbSysSecurityRelUserPos;

@Repository
public interface TbSysSecurityRelUserPosMapper {
    int deleteByPrimaryKey(String relId);

    int insert(TbSysSecurityRelUserPos record);

    int insertSelective(TbSysSecurityRelUserPos record);

    TbSysSecurityRelUserPos selectByPrimaryKey(String relId);

    int updateByPrimaryKeySelective(TbSysSecurityRelUserPos record);

    int updateByPrimaryKey(TbSysSecurityRelUserPos record);
    
    public  List<TbSysSecurityRelUserPos> selectPositionForUserId(String userId);
    
    public List<TbSysSecurityRelUserPos> selectUserForPosition(String positionId);
}