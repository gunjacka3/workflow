package cn.com.workflow.ext.behavior.factory;

import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.engine.impl.bpmn.behavior.ParallelGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

import cn.com.workflow.ext.behavior.ParallelGatewayActivityBehaviorExt2;

/**
 * activiti扩展Gateway工程实现类(不适用多线程问题)
 * Package : cn.com.workflow.ext.behavior
 * 
 * @author wangzhiyin
 *         2017年9月27日 上午11:22:00
 *
 */
public class ParallelGatewayActivityExtFactory extends DefaultActivityBehaviorFactory {

	private ParallelGatewayActivityBehaviorExt2 parallelGatewayActivityBehaviorExt;
	
	/**
	   * 通过Spring容器注入新的分支条件行为执行类
	   * @param exclusiveGatewayActivityBehaviorExt
	   */
	  public void setParallelGatewayActivityBehaviorExt(ParallelGatewayActivityBehaviorExt2 exclusiveGatewayActivityBehaviorExt) {
	    this.parallelGatewayActivityBehaviorExt = exclusiveGatewayActivityBehaviorExt;
	  }
	  
	  //重写父类中的分支条件行为执行类
	  @Override
	  public ParallelGatewayActivityBehavior createParallelGatewayActivityBehavior(ParallelGateway parallelGateway) {
		    return parallelGatewayActivityBehaviorExt;
	  }

}
