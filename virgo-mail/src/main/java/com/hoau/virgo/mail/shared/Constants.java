package com.hoau.virgo.mail.shared;
/**   
* @Title: Constants 
* @Package com.hoau.virgo.mail.shared 
* @Description: 邮件发送相关常量
* @author 陈宇霖  
* @date 2017/9/20 16:16
* @version V1.0   
*/
public class Constants {

    /**
     * 邮件发送类型-异步发送
     */
    public static final int SEND_TYPE_ASYNC = 0;

    /**
     * 邮件发送类型-同步发送
     */
    public static final int SEND_TYPE_SYNC = 1;

    /**
     * 邮件发送类型-异步定时发送
     */
    public static final int SEND_TYPE_FIX_TIME = 2;

    /**
     * 邮件附件存储在ftp服务器上的基目录
     */
    public static final String MAIL_ATTACHMENT_FTP_BASE_DIR = "/virgo/mail/attachment";

    /**
     * 邮件发送状态-待发送
     */
    public static final int SEND_STATUS_WAIT = 0;

    /**
     * 邮件发送状态-发送中
     */
    public static final int SEND_STATUS_SENDING = 1;

    /**
     * 邮件发送状态-发送成功
     */
    public static final int SEND_STATUS_SUCCESS = 2;

    /**
     * 邮件发送状态-发送失败
     */
    public static final int SEND_STATUS_FAILURE = 3;

    /**
     * 每次调度任务最大发送条数
     */
    public static final int MAX_COUNT_PER_TIME = 30;

    /**
     * 最大失败重试次数
     */
    public static final int MAX_RETRY_TIMES = 2;

    /**
     * 失败后允许重试的最大延长分钟数
     */
    public static final int MAX_FAILURE_DELAY_MINUTES = 60;

    /**
     * 多线程发送邮件线程数
     */
    public static final int SEND_MAIL_THREAD_COUNT = 10;
}
