package com.wyl.virgo.mail.shared.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* @Title: MailMessageDTO 
* @Package com.wyl.virgo.mail.shared.dto
* @Description: 发送邮件入参
* @author
* @date 2017/9/19 09:23
* @version V1.0   
*/
@ApiModel(value = "邮件发送参数")
public class Mail implements Serializable{

    private static final long serialVersionUID = -1717123793322338390L;

    /**
     * 邮件发送类型:0异步发送、1实时发送、2定时发送
     */
    private int sendType;

    /**
     * 发件人
     */
    private String from;

    /**
     * 主题
     */
    private String subject;

    /**
     * 收件人
     */
    private String[] to;

    /**
     * 抄送
     */
    private String[] cc;

    /**
     * 秘密抄送
     */
    private String[] bcc;

    /**
     * 邮件文本内容
     */
    private String text;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板数据
     */
    private Map data;

    /**
     * 附件
     */
    private LinkedHashMap<String, byte[]> attachment;

    /**
     * 发送优先级，1-5
     */
    private Integer priority;

    /**
     * 要求发送时间
     */
    private Date requiredSendTime;

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public LinkedHashMap<String, byte[]> getAttachment() {
        return attachment;
    }

    public void setAttachment(LinkedHashMap<String, byte[]> attachment) {
        this.attachment = attachment;
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
}
