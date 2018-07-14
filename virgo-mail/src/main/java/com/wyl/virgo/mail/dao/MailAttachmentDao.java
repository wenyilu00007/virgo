package com.wyl.virgo.mail.dao;

import com.wyl.virgo.mail.shared.po.MailAttachmentPO;
import com.wyl.zodiac.datasource.mybatis.basedao.BaseDao;
import org.springframework.stereotype.Repository;

/**
* @Title: MailDao 
* @Package com.wyl.virgo.mail.dao
* @Description: 邮件相关持久化
* @author
* @date 2017/9/21 07:46
* @version V1.0   
*/
@Repository
public interface MailAttachmentDao extends BaseDao<MailAttachmentPO> {

}
