package com.hoau.virgo.mail.dao;

import com.hoau.virgo.mail.shared.po.MailAttachmentPO;
import com.hoau.zodiac.datasource.mybatis.basedao.BaseDao;
import org.springframework.stereotype.Repository;

/**
* @Title: MailDao 
* @Package com.hoau.virgo.mail.dao 
* @Description: 邮件相关持久化
* @author 陈宇霖  
* @date 2017/9/21 07:46
* @version V1.0   
*/
@Repository
public interface MailAttachmentDao extends BaseDao<MailAttachmentPO> {

}
