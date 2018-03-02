package com.eqdushu.server.controller.cmm;

import com.eqdushu.server.annotation.NonAuthorization;
import com.eqdushu.server.domain.cmm.CheckVersionDto;
import com.eqdushu.server.domain.cmm.ClientVersion;
import com.eqdushu.server.service.cmm.ICmmService;
import com.eqdushu.server.utils.ResponseCode;
import com.eqdushu.server.utils.VersionUtil;
import com.eqdushu.server.vo.Response;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import com.google.common.base.Strings;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by nobita on 17/11/19.
 */
@Controller
@RequestMapping("/cmm")
@LogInfo
@LogException(value = { @LogException.Exc(value = Exception.class, stacktrace = false) }, warn = { @LogException.Exc({ IllegalArgumentException.class }) })
public class CmmController {


    @Autowired
    private ICmmService cmmService;
    /**
     * 检查客户端版本
     * @param params
     * @return
     */
    @NonAuthorization
    @RequestMapping(value = "/checkVersion", method = RequestMethod.POST)
    public @ResponseBody Response checkVersion(@RequestBody Map<String, String> params){

        String casTyp = MapUtils.getString(params, "casTyp"); //客户端类型
        String version = MapUtils.getString(params, "version");//客户端版本号
        if (Strings.isNullOrEmpty(casTyp) || Strings.isNullOrEmpty(version)) {
            return new Response(ResponseCode.invalid_attribute);
        }

        CheckVersionDto versionDto = new CheckVersionDto();
        //查询当前对应客户端的版本号
        casTyp = casTyp.toUpperCase();
        ClientVersion clientVersion = cmmService.quryClientVersion(casTyp);
        versionDto.setUpdateFlag("0");
        //判断如果用户版本号小于当前版本号，则提示更新
        if(VersionUtil.compareStringHavingDot(clientVersion.getCurVersion(),version)){
            versionDto.setUpdateFlag("1");
            versionDto.setDownloadUrl(clientVersion.getDownUrl());
            versionDto.setVersionDetail(clientVersion.getVersionDetail());
            versionDto.setNewVersionCode(clientVersion.getCurVersion());
        }
        //如果用户当前版本号小于最低版本号，则提示强制更新
        if(VersionUtil.compareStringHavingDot(clientVersion.getEarlyVersion(),version)){
            versionDto.setUpdateFlag("2");
            versionDto.setDownloadUrl(clientVersion.getDownUrl());
            versionDto.setVersionDetail(clientVersion.getVersionDetail());
            versionDto.setNewVersionCode(clientVersion.getCurVersion());
        }
        return new Response(ResponseCode.success,versionDto);
    }
}
