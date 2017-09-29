package cn.com.workflow.ext.identity.database.manager;

import javax.servlet.http.HttpSession;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * activiti历史任务扩展实现Manager Package :
 * cn.com.workflow.ext.identity.database.factory
 * 
 * @author wangzhiyin 2017年9月27日 上午11:23:57
 *
 */
@Service("cusHisTaskInsManager")
public class CusHisTaskInsManager extends HistoricTaskInstanceEntityManager {

    @Autowired
    private HttpSession session;

//    @Override
//    public void insert(PersistentObject persistentObject) {
//
//        Users user = (Users) session.getAttribute("user");
//        String usercd = user.getUserCd();
//        String orgcd = user.getOrgCd();
//
//        HistoricTaskInstanceEntity ht = (HistoricTaskInstanceEntity) persistentObject;
//        ht.setAssignee(orgcd);
//        ht.setOwner(orgcd);
//        getDbSqlSession().insert(ht);
//    }
}
