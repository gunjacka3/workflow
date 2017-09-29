package cn.com.workflow.mybatis.client.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.workflow.common.Page;
import cn.com.workflow.mybatis.client.WfmidLoginfoMapper;
import cn.com.workflow.mybatis.model.WfmidLoginfo;


@Repository("wfmidLoginfoMapperImpl")
public class WfmidLoginfoMapperImpl implements WfmidLoginfoMapper {
	
	@Autowired(required=true)
	private SqlSessionTemplate myBatisSession;

	@Override
	public int deleteByPrimaryKey(Long id) {
		return myBatisSession.delete("com.git.baidu.mybatis.client.WfmidLoginfoMapper.deleteByPrimaryKey",id);
	}

	@Override
	public int insert(WfmidLoginfo record) {
		return myBatisSession.insert("com.git.baidu.mybatis.client.WfmidLoginfoMapper.insert", record);
	}

	@Override
	public int insertSelective(WfmidLoginfo record) {
		return myBatisSession.insert("com.git.baidu.mybatis.client.WfmidLoginfoMapper.insertSelective", record);
	}

	@Override
	public WfmidLoginfo selectByPrimaryKey(Long id) {
		return myBatisSession.selectOne("com.git.baidu.mybatis.client.WfmidLoginfoMapper.selectByPrimaryKey", id);
	}

	@Override
	public int updateByPrimaryKeySelective(WfmidLoginfo record) {
		return myBatisSession.update("com.git.baidu.mybatis.client.WfmidLoginfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(WfmidLoginfo record) {
		return myBatisSession.update("com.git.baidu.mybatis.client.WfmidLoginfoMapper.updateByPrimaryKeyWithBLOBs", record);
	}

	@Override
	public int updateByPrimaryKey(WfmidLoginfo record) {
		return myBatisSession.update("com.git.baidu.mybatis.client.WfmidLoginfoMapper.updateByPrimaryKey", record);
	}

	
	public List<WfmidLoginfo> queryList(java.util.Map _map,Page _page) {
		Long count=conutList(_map);
		_page.setTotalRecord(count);
		_map.put("startIndex", _page.getStart_index());
		_map.put("pageSize", _page.getPageSize());
		return myBatisSession.selectList("com.git.baidu.mybatis.client.WfmidLoginfoMapper.queryList", _map);
	}
	
	public WfmidLoginfo infoText(Long id) {
		return myBatisSession.selectOne("com.git.baidu.mybatis.client.WfmidLoginfoMapper.fandText", id);
	}
	
	public Long conutList(java.util.Map _map){
		return myBatisSession.selectOne("com.git.baidu.mybatis.client.WfmidLoginfoMapper.conutListForPage", _map);
	}
	
	
	public List<WfmidLoginfo> queryTextInfoByProcessId(String _processId,String _operationType){
		java.util.Map map=new java.util.HashMap();
		map.put("processId", _processId);
		map.put("operationType", _operationType);
		
		return myBatisSession.selectList("com.git.baidu.mybatis.client.WfmidLoginfoMapper.queryTextInfoByProcessId", map);
	}
	
	
	
	
}
