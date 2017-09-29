package cn.com.workflow.controller.ws.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.workflow.common.Page;
import cn.com.workflow.common.exception.BpmException;
import cn.com.workflow.common.vo.ActHisActInstListPage;
import cn.com.workflow.common.vo.ActHisProcessListPage;
import cn.com.workflow.common.vo.ActWorkListPage;
import cn.com.workflow.controller.ws.ActQueryWebService;
import cn.com.workflow.service.QueryWorkListService;

/**
 * 查询web服务类 Package : cn.com.workflow.controller.ws.impl
 * 
 * @author wangzhiyin 2017年9月25日 上午9:32:53
 *
 */
@Component("actQueryWebServiceImpl")
public class ActQueryWebServiceImpl extends BaseWebService implements ActQueryWebService {

    @Autowired(required = true)
    private QueryWorkListService queryWorkListService;

    /**
     * 校验用字段
     */
    private static final String REQUIRE_STR = "userId,page_index,pageSize";
    /**
     * remove常量userId
     */
    private static final String REMOVE_USERID = "userId";
    /**
     * remove常量userId
     */
    private static final String REMOVE_PAGEINDEX = "page_index";
    /**
     * remove常量pageSize
     */
    private static final String REMOVE_PAGESIZE = "pageSize";

    /**
     * 查询待办任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:33:25
     */
    @Override
    public String queryWorkList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActWorkListPage awp = queryWorkListService.queryWorkList(userId, map, page);
        return object2Jackson(awp);
    }

    /**
     * 查询待办任务总数
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:37:06
     */
    @Override
    public String conutWorkList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Long count = queryWorkListService.conutWorkList(userId, null, null, map);
        return "{\"count\":\"" + count + "\"}";
    }

    /**
     * 查询未分配任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:37:23
     */
    @Override
    public String queryCandiWorkList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActWorkListPage awp = queryWorkListService.queryCandiWorkList(userId, map, page);
        return object2Jackson(awp);
    }

    /**
     * 查询未分配任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:37:35
     */
    @Override
    public String conutCandiWorkList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Long count = queryWorkListService.conutCandiWorkList(userId, map);
        return "{\"count\":\"" + count + "\"}";
    }

    /**
     * 查询挂起任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:37:45
     */
    @Override
    public String querySuspendList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActWorkListPage awp = queryWorkListService.querySuspendList(userId, map, page);
        return object2Jackson(awp);
    }

    /**
     * 查询挂起任务总数
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:38:20
     */
    @Override
    public String conutSuspendList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Long count = queryWorkListService.conutSuspendList(userId, map);
        return "{\"count\":\"" + count + "\"}";
    }

    /**
     * 查询历史人物列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:38:35
     */
    @Override
    public String queryHisList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActHisProcessListPage app = queryWorkListService.queryHisList(userId, map, page);
        return object2Jackson(app);
    }

    /**
     * 查询历史任务总数
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:38:45
     */
    @Override
    public String conutHisList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Long count = queryWorkListService.conutHisList(userId, map);
        return "{\"count\":\"" + count + "\"}";
    }

    /**
     * 查询已结束历史任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:38:57
     */
    @Override
    public String queryFinishHisList(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActHisProcessListPage app = queryWorkListService.queryFinishHisList(userId, map, page);
        return object2Jackson(app);
    }

    /**
     * 查询所有历史任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:39:17
     */
    @Override
    public String queryhisListAll(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActHisProcessListPage app = queryWorkListService.queryhisListAll(map, page);
        return object2Jackson(app);
    }

    /**
     * 查询指定流程实例id所有历史任务列表
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:39:42
     */
    @Override
    public String queryHisTaskList(String _variablesJson) throws BpmException {
        String requiredStr = "userId,processId,page_index,pageSize";
        Map<String, Object> map = getInputDate(_variablesJson, requiredStr);
        map.remove(REMOVE_USERID);
        String processId = (String) map.get("processId");
        map.remove("processId");
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        ActHisActInstListPage atp = queryWorkListService.queryHisTaskList(processId, map, page);
        return object2Jackson(atp);
    }

    /**
     * 查询制定模板所有任务
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:40:53
     */
    @Override
    public String countWorkForTemplate(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Map returnMap = queryWorkListService.countWorkForTemplate(userId);
        return map2Jackson(returnMap);
    }

    /**
     * 查询制定模板所有任务总数
     * 
     * @param _variablesJson
     * @return
     * @throws BpmException
     * @author wangzhiyin 2017年9月25日 上午9:41:26
     */
    @Override
    public String countCandiWorkByTemplate(String _variablesJson) throws BpmException {
        Map<String, Object> map = getInputDate(_variablesJson, REQUIRE_STR);
        String userId = (String) map.get(REMOVE_USERID);
        map.remove(REMOVE_USERID);
        int page_index = (Integer) map.get(REMOVE_PAGEINDEX);
        map.remove(REMOVE_PAGEINDEX);
        int pageSize = (Integer) map.get(REMOVE_PAGESIZE);
        map.remove(REMOVE_PAGESIZE);
        Page page = new Page();
        page.setStart_index(page_index);
        page.setPageSize(pageSize);
        Map returnMap = queryWorkListService.countCandiWorkByTemplate(userId);
        return map2Jackson(returnMap);
    }

}
