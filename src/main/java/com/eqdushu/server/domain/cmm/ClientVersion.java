package com.eqdushu.server.domain.cmm;

import com.eqdushu.server.domain.BaseDomain;

import java.io.Serializable;
import java.util.Date;

public class ClientVersion extends BaseDomain {
    /**
     * BIGINT(20) 必填<br>
     * 
     */
    private Long id;

    /**
     * 客户端类型
     * VARCHAR(8) 默认值[] 必填<br>
     * 
     */
    private String casTyp;

    /**
     * 当前版本号
     * VARCHAR(8) 默认值[] 必填<br>
     * 
     */
    private String curVersion;

    /**
     * 早期版本号，用于强制更新
     * VARCHAR(8) 默认值[] 必填<br>
     * 
     */
    private String earlyVersion;

    /**
     * 下载地址
     * VARCHAR(128) 默认值[] 必填<br>
     * 
     */
    private String downUrl;

    /**
     * 更新提示语
     * VARCHAR(128) 默认值[] 必填<br>
     * 
     */
    private String versionDetail;

    /**
     * TIMESTAMP(19)<br>
     * 
     */
    private Date createTime;

    /**
     * TIMESTAMP(19)<br>
     * 
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * BIGINT(20) 必填<br>
     * 获得 
     */
    public Long getId() {
        return id;
    }

    /**
     * BIGINT(20) 必填<br>
     * 设置 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 获得 
     */
    public String getCasTyp() {
        return casTyp;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 设置 
     */
    public void setCasTyp(String casTyp) {
        this.casTyp = casTyp;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 获得 
     */
    public String getCurVersion() {
        return curVersion;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 设置 
     */
    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 获得 
     */
    public String getEarlyVersion() {
        return earlyVersion;
    }

    /**
     * VARCHAR(8) 默认值[] 必填<br>
     * 设置 
     */
    public void setEarlyVersion(String earlyVersion) {
        this.earlyVersion = earlyVersion;
    }

    /**
     * VARCHAR(128) 默认值[] 必填<br>
     * 获得 
     */
    public String getDownUrl() {
        return downUrl;
    }

    /**
     * VARCHAR(128) 默认值[] 必填<br>
     * 设置 
     */
    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    /**
     * VARCHAR(128) 默认值[] 必填<br>
     * 获得 
     */
    public String getVersionDetail() {
        return versionDetail;
    }

    /**
     * VARCHAR(128) 默认值[] 必填<br>
     * 设置 
     */
    public void setVersionDetail(String versionDetail) {
        this.versionDetail = versionDetail;
    }

    /**
     * TIMESTAMP(19)<br>
     * 获得 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * TIMESTAMP(19)<br>
     * 设置 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * TIMESTAMP(19)<br>
     * 获得 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * TIMESTAMP(19)<br>
     * 设置 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        ClientVersion other = (ClientVersion) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCasTyp() == null ? other.getCasTyp() == null : this.getCasTyp().equals(other.getCasTyp()))
            && (this.getCurVersion() == null ? other.getCurVersion() == null : this.getCurVersion().equals(other.getCurVersion()))
            && (this.getEarlyVersion() == null ? other.getEarlyVersion() == null : this.getEarlyVersion().equals(other.getEarlyVersion()))
            && (this.getDownUrl() == null ? other.getDownUrl() == null : this.getDownUrl().equals(other.getDownUrl()))
            && (this.getVersionDetail() == null ? other.getVersionDetail() == null : this.getVersionDetail().equals(other.getVersionDetail()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCasTyp() == null) ? 0 : getCasTyp().hashCode());
        result = prime * result + ((getCurVersion() == null) ? 0 : getCurVersion().hashCode());
        result = prime * result + ((getEarlyVersion() == null) ? 0 : getEarlyVersion().hashCode());
        result = prime * result + ((getDownUrl() == null) ? 0 : getDownUrl().hashCode());
        result = prime * result + ((getVersionDetail() == null) ? 0 : getVersionDetail().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", casTyp=").append(casTyp);
        sb.append(", curVersion=").append(curVersion);
        sb.append(", earlyVersion=").append(earlyVersion);
        sb.append(", downUrl=").append(downUrl);
        sb.append(", versionDetail=").append(versionDetail);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}