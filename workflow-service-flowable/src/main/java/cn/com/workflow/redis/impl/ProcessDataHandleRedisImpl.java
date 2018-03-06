package cn.com.workflow.redis.impl;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.SerializeUtil;
import cn.com.workflow.redis.ProcessDataHandle;
import cn.com.workflow.redis.RedisService;

@Service(value = "processDataHandleRedisImpl")
public class ProcessDataHandleRedisImpl implements ProcessDataHandle {
	
    @Autowired
    private RedisService redisService;
    
    
    /**
     * 
     * @param _key
     * @return
     */
	public String  getStringData(String _key){
		byte[] b=redisService.gets(_key);
		if(null==b || b.length==0){
			return null;
		}else{
			return (String)SerializeUtil.unserialize(b);
		}
	}

	/**
	 * 
	 * @param keys
	 * @param Data
	 * @throws UnsupportedEncodingException 
	 */
	public void extendStringData(String keys,String Data) throws Exception{
		String track=getStringData(keys);
		if(null!=track && !"".equals(track)){
			redisService.set(keys.getBytes("utf-8"), SerializeUtil.serialize(track+","+Data), 0);
		}else{
			redisService.set(keys.getBytes("utf-8"), SerializeUtil.serialize(Data), 0);
		}
	}
	
	/**
	 * 
	 * @param keys
	 * @param _data
	 */
	public void addData(String keys,String _data) throws Exception{
		try {
			redisService.set(keys.getBytes("utf-8"), SerializeUtil.serialize(_data), 0);
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
	}
	
	/**
	 * 
	 * @param keys
	 * @return
	 */
	public boolean existsData(String keys){
		return redisService.exists(keys);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public Set keys(String str){
		return redisService.keys(str);
	}
	
	/**
	 * 
	 * @param str
	 */
	public void del(String... str){
		redisService.del(str);
	}
}
