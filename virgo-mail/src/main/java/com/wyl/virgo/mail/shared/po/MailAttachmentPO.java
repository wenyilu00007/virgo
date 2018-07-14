package com.wyl.virgo.mail.shared.po;

import com.wyl.zodiac.core.entity.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
* @Title: MailAttachmentPO
* @Package com.wyl.virgo.mail.shared.po
* @Description: 邮件附件表
* @author
* @date 2017/9/20 18:26
* @version V1.0   
*/
@Table(name = "t_mail_attachment")
public class MailAttachmentPO extends BasicEntity {

    /**
     * 邮件主表ID
     */
    private String mailId;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 顺序
     */
    private Integer attachmentOrder;

    /**
     * 附件文件地址
     */
    private String attachmentUrl;


    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public Integer getAttachmentOrder() {
        return attachmentOrder;
    }

    public void setAttachmentOrder(Integer attachmentOrder) {
        this.attachmentOrder = attachmentOrder;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

}
