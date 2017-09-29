package cn.com.workflow.mybatis.model;

import java.util.Date;

public class TbSysSecurityPositions {
    private String positionId;

    private String positionName;

    private String positionCd;

    private String orgGrade;

    private String maintainId;

    private Date createTime;

    private Date updateTime;

    private String delflag;

    private Integer truncNo;

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionCd() {
        return positionCd;
    }

    public void setPositionCd(String positionCd) {
        this.positionCd = positionCd;
    }

    public String getOrgGrade() {
        return orgGrade;
    }

    public void setOrgGrade(String orgGrade) {
        this.orgGrade = orgGrade;
    }

    public String getMaintainId() {
        return maintainId;
    }

    public void setMaintainId(String maintainId) {
        this.maintainId = maintainId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDelflag() {
        return delflag;
    }

    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    public Integer getTruncNo() {
        return truncNo;
    }

    public void setTruncNo(Integer truncNo) {
        this.truncNo = truncNo;
    }
}