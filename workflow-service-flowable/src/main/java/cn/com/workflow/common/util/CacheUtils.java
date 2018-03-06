package cn.com.workflow.common.util;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;

/**
 * 缓存工具类
 * Package : cn.com.workflow.common.util
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午9:42:03
 *
 */
public class CacheUtils {
	
//  private static CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("ehcache"));
//	private static CacheManager cacheManager = ((CacheManager)SpringContextHolder.getBean("cacheManager"));
	
	@Resource
    private static CacheManager cacheManager ;

	private static final String SYS_CACHE = "sysCache";

	/**
	 * 获取SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Object obj = get(SYS_CACHE, key);
		if(obj instanceof ValueWrapper){
			obj = ((ValueWrapper) obj).get();
		}
		return obj;
	}
	
	/**
	 * 写入SYS_CACHE缓存
	 * @param key
	 * @return
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}
	
	/**
	 * 从SYS_CACHE缓存中移除
	 * @param key
	 * @return
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	/**
	 * 获取缓存
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		return getCache(cacheName).get(key);
	}

	/**
	 * 写入缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		getCache(cacheName).put(key, value);
	}

	/**
	 * 从缓存中移除
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).evict(key);
	}
	
	/**
	 * 获得一个Cache，没有则创建一个。
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName){
		return cacheManager.getCache(cacheName);
	}

	/**
	 * 
	 * 获得一个CacheManager
	 * @return 
	 * @author wangzhiyin
	 *	       2017年9月27日 上午9:42:40
	 */
	public static CacheManager getCacheManager() {
		return cacheManager;
	}
}
