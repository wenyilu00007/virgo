package com.wyl.virgo.mail.shared.po;

import com.wyl.zodiac.core.entity.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
* @Title: MailPO
* @Package com.wyl.virgo.mail.shared.po
* @Description: 邮件主表
* @author
* @date 2017/9/20 18:26
* @version V1.0   
*/
@Table(name = "t_mail")
public class MailPO extends BasicEntity {

    /**
     * 发送类型:0异步实时发送、1同步实时发送、2定时发送
     */
    private Integer sendType;

    /**
     * 发件人
     */
    private String sender;

    /**
     * 主题
     */
    private String subject;

    /**
     * 收件人,多个收件人以英文;分割组成的字符串
     */
    private String receiver;

    /**
     * 抄送,多个抄送人以英文;分割组成的字符串
     */
    private String copyTo;

    /**
     * 密送,多个密送人以英文;分割组成的字符串
     */
    private String blindCopyTo;

    /**
     * 邮件模板名称
     */
    private String templateName;

    /**
     * 模板邮件中的数据
     */
    private String templateData;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 是否有附件
     */
    @Column(name = "is_contains_attachment")
    private Integer containsAttachment;

    /**
     * 优先级 1~5
     */
    private Integer priority;

    /**
     * 要求发送时间
     */
    private Date requiredSendTime;

    /**
     * 发送状态:0待发送，1发送中，2发送成功，3发送失败
     */
    private Integer sendStatus;

    /**
     * 实际发送时间
     */
    private Date sendTime;

    /**
     * 发送失败原因
     */
    private String failureReason;

    /**
     * 发送失败次数
     */
    private Integer failureTimes;

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo;
    }

    public String getBlindCopyTo() {
        return blindCopyTo;
    }

    public void setBlindCopyTo(String blindCopyTo) {
        this.blindCopyTo = blindCopyTo;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateData() {
        return templateData;
    }

    public void setTemplateData(String templateData) {
        this.templateData = templateData;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContainsAttachment() {
        return containsAttachment;
    }

    public void setContainsAttachment(Integer containsAttachment) {
        this.containsAttachment = containsAttachment;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getRequiredSendTime() {
        return requiredSendTime;
    }

    public void setRequiredSendTime(Date requiredSendTime) {
        this.requiredSendTime = requiredSendTime;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Integer getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(Integer failureTimes) {
        this.failureTimes = failureTimes;
    }

}
