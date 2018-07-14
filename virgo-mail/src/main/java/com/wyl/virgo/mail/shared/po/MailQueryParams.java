package com.wyl.virgo.mail.shared.po;

import java.util.Date;

/**
* @Title: MailQueryParams 
* @Package com.wyl.virgo.mail.shared.po
* @Description: 查询邮件参数
* @author
* @date 2017/9/22 08:23
* @version V1.0   
*/
public class MailQueryParams {

    /**
     * 要求发送时间-起始
     */
    private Date requiredSendTimeStart;

    /**
     * 要求发送时间-截止
     */
    private Date requiredSendTimeEnd;

    /**
     * 最大行数
     */
    private int maxCount;

    /**
     * 最大失败次数
     */
    private int maxFailureTimes;

    /**
     * 失败后允许重发的最大延时分钟数(超过了此时间，则不再发送)
     */
    private int maxFailureDelayMinutes;

    public Date getRequiredSendTimeStart() {
        return requiredSendTimeStart;
    }

    public void setRequiredSendTimeStart(Date requiredSendTimeStart) {
        this.requiredSendTimeStart = requiredSendTimeStart;
    }

    public Date getRequiredSendTimeEnd() {
        return requiredSendTimeEnd;
    }

    public void setRequiredSendTimeEnd(Date requiredSendTimeEnd) {
        this.requiredSendTimeEnd = requiredSendTimeEnd;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxFailureTimes() {
        return maxFailureTimes;
    }

    public void setMaxFailureTimes(int maxFailureTimes) {
        this.maxFailureTimes = maxFailureTimes;
    }

    public int getMaxFailureDelayMinutes() {
        return maxFailureDelayMinutes;
    }

    public void setMaxFailureDelayMinutes(int maxFailureDelayMinutes) {
        this.maxFailureDelayMinutes = maxFailureDelayMinutes;
    }
}
