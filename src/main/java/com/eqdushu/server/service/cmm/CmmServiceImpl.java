package com.eqdushu.server.service.cmm;

import com.eqdushu.server.domain.cmm.ClientVersion;
import com.eqdushu.server.mapper.cmm.ClientVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nobita on 17/11/19.
 */
@Service
public class CmmServiceImpl implements ICmmService{

    @Autowired
    private ClientVersionMapper clientVersionMapper;

    @Override
    public ClientVersion quryClientVersion(String casTyp) {
        return clientVersionMapper.selectByCasTyp(casTyp);
    }
}
