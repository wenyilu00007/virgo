package com.hoau.virgo.mail.dao;

import com.hoau.virgo.mail.shared.po.MailLastSendTimePO;
import com.hoau.zodiac.datasource.mybatis.basedao.BaseDao;
import org.springframework.stereotype.Repository;

/**
* @Title: MailLastSendTimeDao 
* @Package com.hoau.virgo.mail.dao 
* @Description: 邮件最近发送时间持久化
* @author 陈宇霖  
* @date 2017/9/21 22:28
* @version V1.0   
*/
@Repository
public interface MailLastSendTimeDao extends BaseDao<MailLastSendTimePO> {
}
