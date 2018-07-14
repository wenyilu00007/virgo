package com.wyl.virgo.mail.shared.domain;
/**   
* @Title: MailSendResult 
* @Package com.wyl.virgo.mail.shared.domain
* @Description: 邮件发送结果
* @author
* @date 2017/9/21 15:53
* @version V1.0   
*/
public class MailSendResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 失败原因
     */
    private String errorReason;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
