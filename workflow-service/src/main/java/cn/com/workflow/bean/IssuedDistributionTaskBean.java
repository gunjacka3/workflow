package cn.com.workflow.bean;

import java.util.List;
import java.util.Set;

import org.activiti.engine.EngineServices;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.com.workflow.mybatis.client.TbSysSecurityRelUserPosMapper;
import cn.com.workflow.mybatis.model.TbSysSecurityRelUserPos;

/**
 * 工作流节点监听器:任务分配类 Package : cn.com.workflow.bean
 * 
 * @author wangzhiyin 2017年9月26日 上午9:36:18
 *
 */
@Service("issuedDistributionTaskBean")
public class IssuedDistributionTaskBean {

    private static Logger log = LoggerFactory.getLogger(IssuedDistributionTaskBean.class);

    @Autowired
    private TbSysSecurityRelUserPosMapper tbSysSecurityRelUserPosMapperImpl;

    /**
     * 下发到岗位任务自动分配到人,须考虑未分配到指定人,而是由用户自己领取任务的情况.
     * 
     * @param task
     * @param execution
     */
    public void distributionTask(DelegateTask task, DelegateExecution execution) {
        log.debug("distributionTask===>{}", task);
        EngineServices engineServices = execution.getEngineServices();
        // 按照指定用户id分发任务
        boolean flag = assigneeForIssued(engineServices, task, execution);
        if (flag) {
            return;
        } else {
            // 指定用户无法分发,按照历史记录分发
            assigneeForHistory(engineServices, task);
        }

    }

    /**
     * 按历史记录分发任务(退回任务)
     * 
     * @param engineServices
     * @param task
     * @param execution
     * @return
     */
    private boolean assigneeForHistory(EngineServices engineServices, DelegateTask task) {

        List<HistoricTaskInstance> htasks = engineServices.getHistoryService().createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).taskDefinitionKey(task.getTaskDefinitionKey())
                .orderByTaskCreateTime().desc().list();
        if (!CollectionUtils.isEmpty(htasks)) {
            task.setAssignee(htasks.get(0).getAssignee());
            return true;
        } else {
            return false;
        }

    }

    /**
     * 按照指定节点岗位,判断岗位下是否存在指定用户,若用户存在则进行任务自动领取
     * 
     * @param engineServices
     * @param task
     * @param execution
     * @return
     */
    private boolean assigneeForIssued(EngineServices engineServices, DelegateTask task, DelegateExecution execution) {
        // 没有用户id,不进行任务分配
        Object obj = execution.getVariable("personalId");
        if (null == obj) {
            return false;
        }
        String userId = (String) obj;
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        Set<Expression> groups = findTaskGroupName(engineServices, task.getProcessDefinitionId(),
                task.getTaskDefinitionKey());
        for (Expression group : groups) {

            List<TbSysSecurityRelUserPos> pos = tbSysSecurityRelUserPosMapperImpl
                    .selectUserForPosition(group.getExpressionText());
            if (null != pos) {
                for (TbSysSecurityRelUserPos po : pos) {
                    if (po.getUserId().equals(userId)) {
                        engineServices.getTaskService().claim(task.getId(), userId);
                        // task.setAssignee(userId);
                        return true;
                    }
                }
            }

        }

        return false;
    }

    /**
     * 查询节点岗位配置信息
     * 
     * @param engineServices
     * @param procDefId
     * @param taskDefId
     * @return
     */
    private Set<Expression> findTaskGroupName(EngineServices engineServices, String procDefId, String taskDefId) {
        RepositoryService repositoryService = engineServices.getRepositoryService();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(procDefId);
        ActivityImpl activityImpl = processDefinition.findActivity(taskDefId);
        PvmActivity pa = activityImpl;
        TaskDefinition taskDefinition = (TaskDefinition) pa.getProperty("taskDefinition");
        return taskDefinition.getCandidateGroupIdExpressions();
    }
}
