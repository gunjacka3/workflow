package cn.com.workflow.service;

import org.springframework.stereotype.Service;

import cn.com.workflow.common.vo.chart.ActInstanceStatistcsEchartsVO;

@Service("statisticsService")
public interface StatisticsService {

    /**
     *  查询流程实例数据
     * 
     * @return 
     * @author wangzhiyin
     *	       2017年6月19日 下午5:36:21
     */
    public ActInstanceStatistcsEchartsVO findProcessInstanceCount();
    
    
    
}
