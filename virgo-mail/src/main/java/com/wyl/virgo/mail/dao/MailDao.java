package com.wyl.virgo.mail.dao;

import com.wyl.virgo.mail.shared.po.MailPO;
import com.wyl.virgo.mail.shared.po.MailQueryParams;
import com.wyl.zodiac.datasource.mybatis.basedao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @Title: MailDao 
* @Package com.wyl.virgo.mail.dao
* @Description: 邮件相关持久化
* @author
* @date 2017/9/21 07:46
* @version V1.0   
*/
@Repository
public interface MailDao extends BaseDao<MailPO> {

    /**
     * 查询需要发送的
     * @param queryParams
     * @return
     * @author
     * @date 2017年09月25日09:51:58
     */
    List<MailPO> queryNeedSendMails(MailQueryParams queryParams);

    /**
     * 更新邮件发送成功
     * @param successResult 需要更新的内容
     * @author
     * @date 2017年09月25日08:00:14
     */
    void updateSendSuccess(MailPO successResult);

    /**
     * 更新发送失败状态
     * @param failureResult 发送失败的信息
     * @author
     * @date 2017年09月25日07:59:59
     */
    void updateSendFailure(MailPO failureResult);
}
