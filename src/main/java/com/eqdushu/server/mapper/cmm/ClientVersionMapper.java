package com.eqdushu.server.mapper.cmm;

import com.eqdushu.server.domain.cmm.ClientVersion;
import org.springframework.stereotype.Repository;

/**
 * Created by nobita on 17/11/19.
 */
@Repository
public interface ClientVersionMapper {

    public ClientVersion selectByCasTyp(String casTyp);
}
