package cn.com.workflow.common;

public class RedisContent {
	
	/**
	 * 系统共享内存存储
	 */
	/*
	 * 主缓存记录地址
	 */
	public static final String REDIS_TOMCAT_SESSION_PROCESSCONFIGURE_DATA="tomcat.session.processConfigure.data";
	/*
	 * 主缓存记录版本地址
	 */
	public static final String REDIS_TOMCAT_SESSION_PROCESSCONFIGURE_VERSION="tomcat.session.processConfigure.version";
	
	
	
	/**
	 * 临时混存——保存分支并行数量
	 */
	/*
	 * 分支触发轨迹-随节点调用数增长
	 */
	public static final String REDIS_PROCESS_FORK_ACTIVITY_TRACK="process.fork.activity.track.all";
	/*
	 * 已完成分支轨迹-随服务调用增长
	 */
	public static final String REDIS_PROCESS_FORK_EXECUTIVE_TRACK="process.fork.activity.track";
	/*
	 * 已完成分支返回结果(集合)-随服务调用增长
	 */
	public static final String REDIS_PROCESS_FORK_EXECUTIVE_DATA="process.fork.activity.data";
	/*
	 * 流程结束删除数据时使用查询key，结合通配符*使用
	 */
	public static final String REDIS_PROCESS_FORK_FINISH_DELETE_KEY="process.fork.activity";	
	
	
	
}
