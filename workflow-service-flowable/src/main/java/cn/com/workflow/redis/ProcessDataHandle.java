package cn.com.workflow.redis;

import java.util.Set;

public interface ProcessDataHandle {

	
	public String  getStringData(String _key);
	
	public void extendStringData(String keys,String Data) throws Exception;
	
	public void addData(String keys,String _data)throws Exception;
	
	public boolean existsData(String keys);
	
	public Set keys(String str);
	
	public void del(String... str);
}
