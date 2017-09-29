package cn.com.workflow.common.vo.chart;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * Package : activiti_maven_project.com.git.common.vo.chart
 * 
 * @author YixinCapital -- wangzhiyin
 *		   2017年6月19日 下午2:57:18
 *
 */
public class ActChartsSeriesVO implements Serializable{

    private static final long serialVersionUID = 1L;

    //名称
    private String name;
    
    //类型
    private String type;
        
    //数据
    private List<String> data;

    //bar宽
    private String barWidth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(String barWidth) {
        this.barWidth = barWidth;
    }
    
    
}
