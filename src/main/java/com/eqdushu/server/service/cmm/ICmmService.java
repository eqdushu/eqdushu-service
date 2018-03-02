package com.eqdushu.server.service.cmm;

import com.eqdushu.server.domain.cmm.ClientVersion;

/**
 * Created by nobita on 17/11/19.
 */
public interface ICmmService {

    /**
     * 查询版本信息
     * @param casTyp
     * @return
     */
    public ClientVersion quryClientVersion(String casTyp);
}
