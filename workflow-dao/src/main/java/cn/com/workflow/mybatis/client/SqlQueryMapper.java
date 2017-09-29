package cn.com.workflow.mybatis.client;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import cn.com.workflow.mybatis.BaseMapper;



@Repository("sqlQueryMapper")
public interface SqlQueryMapper extends BaseMapper {

	public List<java.util.Map<String,Object>> countWorkByTemplate(String userId);

	public List<java.util.Map<String,Object>> countCandiWorkByTemplate(String userId,String[] groupIds);
	
	public List<Map<String,Object>> countPIForChart();
}
