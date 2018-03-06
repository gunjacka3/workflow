package cn.com.workflow.ext.cmd;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.FlowableEngineAgenda;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.flowable.task.service.impl.persistence.entity.TaskEntityManager;

/**
 * 
 * Package : cn.com.workflow.ext.cmd
 * 
 * @author wangzhiyin
 *		   2017年11月2日 上午11:48:27
 *
 */
public class JumpTaskCmd implements Command<Void> {
    protected String taskId;
    protected String target;

    public JumpTaskCmd(String taskId, String target) {
        this.taskId = taskId;
        this.target = target;
    }
    
    public Void execute(CommandContext commandContext) {
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager();
        TaskEntityManager taskEntityManager = org.flowable.task.service.impl.util.CommandContextUtil.getTaskEntityManager();
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        ExecutionEntity ee = executionEntityManager.findById(taskEntity.getExecutionId());
        org.flowable.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(ee.getProcessDefinitionId());
        FlowElement targetFlowElement = process.getFlowElement(target);
        ee.setCurrentFlowElement(targetFlowElement);
        FlowableEngineAgenda agenda = CommandContextUtil.getAgenda();
        agenda.planContinueProcessInCompensation(ee);
        taskEntityManager.delete(taskId);
        return null;
    }
}
