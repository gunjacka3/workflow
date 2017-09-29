package cn.com.workflow.common.vo.chart;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Package : activiti_maven_project.com.git.common.vo.chart
 * 
 * @author YixinCapital -- wangzhiyin
 *		   2017年6月19日 下午2:59:51
 *
 */
public class ActInstanceStatistcsEchartsVO implements Serializable{
    private static final long serialVersionUID = 1L;
    
    //图形名称
    private String text;
    
    //图像标题
    private String subtext;
    
    //图像纵向数据
    private ActChartsYAxisVO yAxis;
    
    //图像显示数据
    private ActChartsSeriesVO series;
    
    //图例
    private String[] legend;
        
    //模板名称
    private String templateName;
    
    //流程模板定义Id
    private String fkBpmProcedefId;
    
    //流程模板Key
    private String fkBpmProcedefKey;
    
    //模板是否删除
    private boolean templateDel;
    
    //实例是否删除
    private boolean instanceDel;
    
    //开始时间
    private Date startTime;
    
    //结束时间
    private Date endTime;

    private String startTimeStr;

    private String endTimeStr;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public ActChartsYAxisVO getyAxis() {
        return yAxis;
    }

    public void setyAxis(ActChartsYAxisVO yAxis) {
        this.yAxis = yAxis;
    }

    public ActChartsSeriesVO getSeries() {
        return series;
    }

    public void setSeries(ActChartsSeriesVO series) {
        this.series = series;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFkBpmProcedefId() {
        return fkBpmProcedefId;
    }

    public void setFkBpmProcedefId(String fkBpmProcedefId) {
        this.fkBpmProcedefId = fkBpmProcedefId;
    }

    public String getFkBpmProcedefKey() {
        return fkBpmProcedefKey;
    }

    public void setFkBpmProcedefKey(String fkBpmProcedefKey) {
        this.fkBpmProcedefKey = fkBpmProcedefKey;
    }

    public boolean isTemplateDel() {
        return templateDel;
    }

    public void setTemplateDel(boolean templateDel) {
        this.templateDel = templateDel;
    }

    public boolean isInstanceDel() {
        return instanceDel;
    }

    public void setInstanceDel(boolean instanceDel) {
        this.instanceDel = instanceDel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }
    
    
    
    
}
