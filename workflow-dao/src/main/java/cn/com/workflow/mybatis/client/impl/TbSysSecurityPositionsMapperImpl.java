package cn.com.workflow.mybatis.client.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.workflow.mybatis.client.TbSysSecurityPositionsMapper;
import cn.com.workflow.mybatis.model.TbSysSecurityPositions;

@Repository("tbSysSecurityPositionsMapperImpl")
public class TbSysSecurityPositionsMapperImpl implements TbSysSecurityPositionsMapper {

	@Autowired(required=true)
	private SqlSessionTemplate myBatisSession;
	@Override
	public int deleteByPrimaryKey(String positionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(TbSysSecurityPositions record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(TbSysSecurityPositions record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TbSysSecurityPositions selectByPrimaryKey(String positionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(TbSysSecurityPositions record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(TbSysSecurityPositions record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
