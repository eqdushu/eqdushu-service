package com.eqdushu.server.domain.cmm;

import com.eqdushu.server.domain.BaseDto;

/**
 * Created by nobita on 17/11/19.
 */
public class CheckVersionDto extends BaseDto{

    private String updateFlag;// 更新标志 0：无新版本提示  1：有新版本提示  2强制更新

    private String newVersionCode;//新版本号

    private String versionDetail;//版本提示语

    private String downloadUrl;//下载链接

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getNewVersionCode() {
        return newVersionCode;
    }

    public void setNewVersionCode(String newVersionCode) {
        this.newVersionCode = newVersionCode;
    }

    public String getVersionDetail() {
        return versionDetail;
    }

    public void setVersionDetail(String versionDetail) {
        this.versionDetail = versionDetail;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
