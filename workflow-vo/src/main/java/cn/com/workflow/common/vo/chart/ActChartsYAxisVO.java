package cn.com.workflow.common.vo.chart;

import java.io.Serializable;
import java.util.List;

public class ActChartsYAxisVO implements Serializable{
    
    private static final long serialVersionUID = 1L;

    //类型
    private String type;
    
    //图像数据
    private List<String> data;

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
    
    

}
