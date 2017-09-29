package cn.com.workflow.ext.service;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import cn.com.workflow.service.ProcessService;

@Service("actServiceAsynCall")
public class ActServiceAsynCall  implements JavaDelegate{
	static Logger logger = LogManager.getLogger();

	@Resource
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Resource
	private ProcessService processService;
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		taskExecutor.execute(new AsynCall(execution,processService));
		
	}
	
	
	/**
	 * 
	 * @author v_wangzhiyin
	 *
	 */
	private class AsynCall  implements Runnable{
		private DelegateExecution execution;
		
		private ProcessService processService;
		public AsynCall(DelegateExecution execution,ProcessService processService){
			this.execution=execution;
			this.processService=processService;
		}
		

		@Override
		public void run() {
			String executionId=execution.getId();
			try {
				Thread.sleep(10000);
				logger.debug("----run----");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			processService.signalProcess(executionId,null);
//			this.execute(executionId, null);
		}
		
	}
	
}
