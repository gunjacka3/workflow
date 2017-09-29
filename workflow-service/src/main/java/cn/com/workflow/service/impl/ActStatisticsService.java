package cn.com.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import cn.com.workflow.common.vo.chart.ActChartsSeriesVO;
import cn.com.workflow.common.vo.chart.ActChartsYAxisVO;
import cn.com.workflow.common.vo.chart.ActInstanceStatistcsEchartsVO;
import cn.com.workflow.service.StatisticsService;

@Service("actStatisticsService")
public class ActStatisticsService implements StatisticsService {
    
    private static final Logger LOGGER = LogManager.getLogger(ActStatisticsService.class);

    @Resource
    private ActQueryWorkListService actQueryWorkListService;
    
    
    @Override
    public ActInstanceStatistcsEchartsVO findProcessInstanceCount() {
        
        
        List<Map<String, Object>> list=actQueryWorkListService.findProcessInstanceCountChart();
        
        //组装echarts数据格式
        List<String> yAxisData = new ArrayList<>();
        List<String> seriesData = new ArrayList<>();
        
        
        for(Map<String, Object>map:list){
            yAxisData.add((String)map.get("pdi"));
            seriesData.add(map.get("con").toString());
        }
        
        
        ActChartsYAxisVO yAxisDTO = new ActChartsYAxisVO();
        ActChartsSeriesVO seriesDTO = new ActChartsSeriesVO();
        ActInstanceStatistcsEchartsVO aiseVO = new ActInstanceStatistcsEchartsVO();
        
        yAxisDTO.setType("category");
        yAxisDTO.setData(yAxisData);
        
        seriesDTO.setType("bar");
        seriesDTO.setBarWidth("60%");
        seriesDTO.setName("实例总数");
        seriesDTO.setData(seriesData);
        
        aiseVO.setyAxis(yAxisDTO);
        aiseVO.setSeries(seriesDTO);
        return aiseVO;
    }

}
