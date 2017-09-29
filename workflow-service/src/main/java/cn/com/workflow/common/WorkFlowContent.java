package cn.com.workflow.common;

/**
 * 工作流常量类
 * Package : cn.com.workflow.common
 * 
 * @author wangzhiyin
 *		   2017年9月27日 上午11:21:22
 *
 */
public class WorkFlowContent {
	
	public static final String WF_TASK_SUBMIT_Type_COMPLETE="complete";
	
	public static final String WF_TASK_SUBMIT_Type_PREVIOUS="previous";
	
	public static final String WF_TASK_SUBMIT_Type_APPOINT="appoint";
	
	public static final String WF_TASK_SUBMIT_Type_FIRST="first";
	
	public static final String WF_TASK_SUBMIT_Type_CANCEL="cancel";
	
	public static final String WF_TASK_SUBMIT_Type_SAVE="save";
	
	
	/*
	 * 是
	 */
	public static final String WF_NOMAL_YES="1";
	/*
	 * 否
	 */
	public static final String WF_NOMAL_NO="0";
	
	/*
	 * 用户编号
	 */
	public static final String WF_USER_NUM="userNum";
	/*
	 * 用户名称
	 */	
	public static final String WF_USER_NAME="userName";
	/*
	 * 角色名称
	 */	
	public static final String WF_ROLE_NAME="roleName";
	/*
	 * 角色代码
	 */	
	public static final String WF_ROLE_CD="roleCd";
	
	/*
	 * 流程发起人
	 */
	public static final String WF_PROCESS_CREATE_NUM="processCreator";
	/*
	 * 路由变量名称
	 */
	public static final String WF_RULE_RESULT_NAME="ruleResult";
	
	/*
	 * 业务类型变量名称
	 */
	public static final String WF_BIZTYPE_NAME="bizType";
	

	/*
	 * 内部敏感数据保存使用des方式的 key值 
	 */
	public static final String WF_SAVE_DATE_DES_KEY="saveDateDesKey";
	
	
	public static final String WF_DEFINITION_FILE_NAME="/definition.properties";
	
	public static final String WF_SESSION_TYPE="redis";
	
	public static final String WF_VERIABLES_NAME_VERSION="version";
	
}
