package com.eqdushu.server.mapper.sms;

import com.eqdushu.server.domain.sms.Sms;
import org.springframework.stereotype.Repository;

/**
 * Created by nobita on 17/10/14.
 */
@Repository
public interface SmsMapper {

    /**登记短信流水
     * @param sms
     * @return
     */
    public int createSmsJrl(Sms sms);

}
