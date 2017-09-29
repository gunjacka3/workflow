package cn.com.workflow.ext.identity.database.manager;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.workflow.mybatis.client.TbSysSecurityUsersMapper;
import cn.com.workflow.mybatis.model.TbSysSecurityUsers;

/**
 * activiti用户扩展实现Manager Package : cn.com.workflow.ext.identity.database.factory
 * 
 * @author wangzhiyin 2017年9月27日 上午11:23:57
 *
 */
@Service("cusActUserManager")
public class CusActUserManager extends UserEntityManager {
    @Autowired
    private TbSysSecurityUsersMapper tbSysSecurityUsersMapperImpl;

    static Logger logger = LogManager.getLogger();

    @Override
    public User findUserById(String userId) {
        UserEntity ue = new UserEntity();
        TbSysSecurityUsers tu = tbSysSecurityUsersMapperImpl.selectByPrimaryKey(userId);
        ue.setFirstName(tu.getUserName());
        ue.setId(tu.getUserId());
        ue.setLastName(StringUtils.EMPTY);

        return ue;
    }

}
