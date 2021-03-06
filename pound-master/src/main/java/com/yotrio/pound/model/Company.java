package com.yotrio.pound.model;

import java.io.Serializable;
import java.util.Date;

public class Company implements Serializable {
    /**
     * 自增id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * 编码
     *
     * @mbg.generated
     */
    private String compCode;

    /**
     * 组织名称
     *
     * @mbg.generated
     */
    private String compName;

    /**
     * 类型
     *
     * @mbg.generated
     */
    private Integer types;

    /**
     * 状态
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * 描述
     *
     * @mbg.generated
     */
    private String description;

    /**
     * 更新时间
     *
     * @mbg.generated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", compCode=").append(compCode);
        sb.append(", compName=").append(compName);
        sb.append(", types=").append(types);
        sb.append(", status=").append(status);
        sb.append(", description=").append(description);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Company other = (Company) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCompCode() == null ? other.getCompCode() == null : this.getCompCode().equals(other.getCompCode()))
            && (this.getCompName() == null ? other.getCompName() == null : this.getCompName().equals(other.getCompName()))
            && (this.getTypes() == null ? other.getTypes() == null : this.getTypes().equals(other.getTypes()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompCode() == null) ? 0 : getCompCode().hashCode());
        result = prime * result + ((getCompName() == null) ? 0 : getCompName().hashCode());
        result = prime * result + ((getTypes() == null) ? 0 : getTypes().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}