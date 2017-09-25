package com.hoau.virgo.mail.shared.po;

import com.hoau.zodiac.core.entity.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
* @Title: MailLastSendTimePO
* @Package com.hoau.virgo.mail.shared.po 
* @Description: 邮件调度任务上次发送时间
* @author 陈宇霖  
* @date 2017/9/21 22:22
* @version V1.0   
*/
@Table(name = "t_mail_last_send_time")
public class MailLastSendTimePO extends BasicEntity {

    /**
     * 上次执行时间
     */
    private Date lastExecTime;

    /**
     * 是否有效
     */
    @Column(name = "is_active")
    private Integer active;

    public Date getLastExecTime() {
        return lastExecTime;
    }

    public void setLastExecTime(Date lastExecTime) {
        this.lastExecTime = lastExecTime;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
