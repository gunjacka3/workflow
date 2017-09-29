package cn.com.workflow.mybatis.client;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.workflow.common.Page;
import cn.com.workflow.mybatis.BaseMapper;
import cn.com.workflow.mybatis.model.WfmidLoginfo;


@Repository("wfmidLoginfoMapper")
public interface WfmidLoginfoMapper extends BaseMapper{
	public int deleteByPrimaryKey(Long id);

    public int insert(WfmidLoginfo record);

    public int insertSelective(WfmidLoginfo record);

    public WfmidLoginfo selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(WfmidLoginfo record);

    public int updateByPrimaryKeyWithBLOBs(WfmidLoginfo record);

    public int updateByPrimaryKey(WfmidLoginfo record);
    public List<WfmidLoginfo> queryTextInfoByProcessId(String _processId,String _operationType);
    public List<WfmidLoginfo> queryList(java.util.Map _map,Page _page);
    public WfmidLoginfo infoText(Long id);
}