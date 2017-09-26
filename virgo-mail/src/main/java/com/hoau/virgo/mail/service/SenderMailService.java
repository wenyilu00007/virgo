package com.hoau.virgo.mail.service;

import com.alibaba.fastjson.JSON;
import com.hoau.virgo.mail.dao.MailAttachmentDao;
import com.hoau.virgo.mail.dao.MailDao;
import com.hoau.virgo.mail.dao.MailLastSendTimeDao;
import com.hoau.virgo.mail.shared.Constants;
import com.hoau.virgo.mail.shared.domain.Mail;
import com.hoau.virgo.mail.shared.domain.MailSendResult;
import com.hoau.virgo.mail.shared.po.MailAttachmentPO;
import com.hoau.virgo.mail.shared.po.MailPO;
import com.hoau.virgo.mail.shared.po.MailLastSendTimePO;
import com.hoau.virgo.mail.shared.po.MailQueryParams;
import com.hoau.virgo.proxy.virgo.IdGenerator;
import com.hoau.zodiac.core.constant.ApplicationConstants;
import com.hoau.zodiac.core.exception.BusinessException;
import com.hoau.zodiac.core.util.FtpUtils;
import com.hoau.zodiac.core.util.date.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* @Title: SenderMailService 
* @Package com.hoau.virgo.mail.service 
* @Description: 邮件发送服务
* @author 陈宇霖  
* @date 2017/9/19 16:02
* @version V1.0   
*/
@Service
public class SenderMailService {

    protected static final Logger logger = LoggerFactory.getLogger(SenderMailService.class);

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration configuration;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private Environment environment;

    @Autowired
    private MailDao mailDao;

    @Autowired
    private MailAttachmentDao mailAttachmentDao;

    @Autowired
    private MailLastSendTimeDao mailLastSendTimeDao;

    @Autowired
    private FtpUtils ftpUtils;

    /**
     * 创建多线程发送邮件线程池
     */
    private ExecutorService service = Executors.newFixedThreadPool(Constants.SEND_MAIL_THREAD_COUNT);

    /**
     * 直接发送邮件
     * @param message
     * @author 陈宇霖
     * @date 2017年09月19日17:12:59
     */
    @Transactional
    public MailSendResult sendMailSync(Mail message) {
        setMailDefaultValue(message);
        // 首先将邮件存储到服务器,状态为发送中
        MailPO savedMailPO = saveMail(message, true);
        // 发送邮件
        MailSendResult result = new MailSendResult();
        try {
            sendMail(message);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            String fullStack = ExceptionUtils.getFullStackTrace(e);
            if (fullStack.length() > 5000) {
                fullStack = fullStack.substring(0, 4800);
            }
            result.setErrorReason(fullStack);
        }
        //更新状态
        MailPO dataToUpdate = new MailPO();
        dataToUpdate.setId(savedMailPO.getId());
        dataToUpdate.setSendStatus(result.isSuccess() ? Constants.SEND_STATUS_SUCCESS : Constants.SEND_STATUS_WAIT);
        dataToUpdate.setFailureReason(result.isSuccess() ? null : result.getErrorReason());
        dataToUpdate.setFailureTimes(result.isSuccess() ? 0 : 1);
        dataToUpdate.setModifyTime(new Date());
        dataToUpdate.setModifyUserCode(ApplicationConstants.SYSTEM_OPERATOR);
        mailDao.updateByPrimaryKeySelective(dataToUpdate);
        return result;
    }

    /**
     * 异步发送邮件，直接存储到表中，由调度任务来发送
     * @param message
     * @author 陈宇霖
     * @date 2017年09月19日17:15:35
     */
    @Transactional
    public void sendMailAsync(Mail message) {
        setMailDefaultValue(message);
        // 只邮件存储到服务器,状态为待发送
        saveMail(message, false);
    }

    /**
     * 调度任务定时发送异步的邮件
     * @author 陈宇霖
     * @date 2017年09月19日17:17:10
     */
    @Transactional
    public void scheduleSendMail() {
        //此次处理的数据截止时间点
        Date thisTime = new Date();
        //获取上次处理时间
        Example example = new Example(MailLastSendTimePO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andCondition("is_active = ", ApplicationConstants.ACTIVE);
        example.setOrderByClause("create_time desc");
        List<MailLastSendTimePO> times = mailLastSendTimeDao.selectByExample(example);
        Date lastTime = null;
        if (!CollectionUtils.isEmpty(times)) {
            lastTime = times.get(0).getLastExecTime();
        } else {
            lastTime = new Date(0);
        }
        //获取从上次发送时间到本次处理截止时间点之间的需要发送的数据
        MailQueryParams queryParams = new MailQueryParams();
        queryParams.setRequiredSendTimeStart(lastTime);
        queryParams.setRequiredSendTimeEnd(thisTime);
        queryParams.setMaxCount(Constants.MAX_COUNT_PER_TIME);
        queryParams.setMaxFailureTimes(Constants.MAX_RETRY_TIMES);
        queryParams.setMaxFailureDelayMinutes(Constants.MAX_FAILURE_DELAY_MINUTES);
        List<MailPO> mailPOS = mailDao.queryNeedSendMails(queryParams);
        //发送邮件
        if (!CollectionUtils.isEmpty(mailPOS)) {
            for (int i = 0; i < mailPOS.size(); i++) {
                sendMailAndSaveResult(mailPOS.get(i));
                //使用多线程发送邮件，
                //TODO 多线程情况下单例的ftpclient会有问题，连接会被另外的线程关闭掉，需要再修改ftp再开启多线程
//                service.submit(new SendMailThread(mailPOS.get(i)));
            }
        }
        //设置最近发送时间
        if (!CollectionUtils.isEmpty(times)) {
            MailLastSendTimePO update = new MailLastSendTimePO();
            update.setId(times.get(0).getId());
            update.setActive(ApplicationConstants.INACTIVE);
            update.setModifyTime(new Date());
            update.setModifyUserCode(ApplicationConstants.SYSTEM_OPERATOR);
            mailLastSendTimeDao.updateByPrimaryKeySelective(update);
        }
        MailLastSendTimePO newLastTime = new MailLastSendTimePO();
        newLastTime.setId(idGenerator.nextId());
        newLastTime.setLastExecTime(thisTime);
        newLastTime.setActive(ApplicationConstants.ACTIVE);
        newLastTime.setCreateTime(new Date());
        newLastTime.setCreateUserCode(ApplicationConstants.SYSTEM_OPERATOR);
        mailLastSendTimeDao.insert(newLastTime);
    }

    /**
     * 设置默认值
     * @param message
     * @author 陈宇霖
     * @date 2017年09月21日18:28:37
     */
    private void setMailDefaultValue(Mail message) {
        //如果没有给发件人，则设置默认发件人，如果没有则报错
        if (StringUtils.isEmpty(message.getFrom())) {
            String defaultFrom = environment.getProperty("spring.mail.defaultFrom");
            if (!StringUtils.isEmpty(defaultFrom)) {
                message.setFrom(defaultFrom);
            } else {
                throw new BusinessException("validator.mail.from.NotNull");
            }
        }
        //要求发送时间，如果空，则设置当前时间
        if (message.getRequiredSendTime() == null) {
            message.setRequiredSendTime(new Date());
        }
        //邮件优先级必须是1-5
        Integer priority = message.getPriority();
        if (priority == null || priority < 1 || priority > 5) {
            message.setPriority(1);
        }
    }

    /**
     * 将邮件信息存储到数据库、附件存储到ftp服务器
     * @param message
     * @author 陈宇霖
     * @date 2017年09月21日15:16:29
     */
    private MailPO saveMail(Mail message, boolean sync) {
        //将附件上传到ftp服务器
        LinkedHashMap<String, String> attachmentPaths = null;
        if (!CollectionUtils.isEmpty(message.getAttachment())) {
            try {
                attachmentPaths = saveAttachment(message.getAttachment());
            } catch (IOException e) {
                logger.error("Update Attachment To Ftp Error", e);
                throw new BusinessException("mail.upload.attachment.error");
            }
        }
        //保存邮件主表
        MailPO mailPO = convertMailDomain2Po(message, sync);
        mailDao.insert(mailPO);
        //保存附件表
        if (!CollectionUtils.isEmpty(attachmentPaths)) {
            Date createTime = new Date();
            Iterator<Map.Entry<String, String>> iterable = attachmentPaths.entrySet().iterator();
            while (iterable.hasNext()) {
                int index = 0;
                Map.Entry<String, String> entity = iterable.next();
                MailAttachmentPO attachment = new MailAttachmentPO();
                attachment.setId(idGenerator.nextId());
                attachment.setMailId(mailPO.getId());
                attachment.setAttachmentName(entity.getKey());
                attachment.setAttachmentUrl(entity.getValue());
                attachment.setAttachmentOrder(index++);
                attachment.setActive(ApplicationConstants.ACTIVE);
                attachment.setCreateTime(createTime);
                attachment.setCreateUserCode(ApplicationConstants.SYSTEM_OPERATOR);
                mailAttachmentDao.insert(attachment);
            }
        }
        return mailPO;
    }

    /**
     * 实际发送邮件的方法
     * @param mail
     * @author 陈宇霖
     * @date 2017年09月21日15:24:02
     */
    private void sendMail(Mail mail) throws MessagingException, IOException, TemplateException {
        //设置邮件名超长时不进行截取，防止中文乱码
        System.setProperty("mail.mime.splitlongparameters","false");
        //判断是否带附件或者是模板邮件
        if (StringUtils.isEmpty(mail.getTemplateName()) && CollectionUtils.isEmpty(mail.getAttachment())) {
            //简单文本邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mail.getFrom());
            message.setTo(mail.getTo());
            if (mail.getCc() != null && mail.getCc().length > 0) {
                message.setCc(mail.getCc());
            }
            if (mail.getBcc() != null && mail.getBcc().length > 0) {
                message.setBcc(mail.getBcc());
            }
            message.setSubject(mail.getSubject());
            message.setText(mail.getText());
            sender.send(message);
        } else {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(mail.getFrom());
            helper.setSubject(mail.getSubject());
            helper.setTo(mail.getTo());
            if (mail.getCc() != null && mail.getCc().length > 0) {
                helper.setCc(mail.getCc());
            }
            if (mail.getBcc() != null && mail.getBcc().length > 0) {
                helper.setBcc(mail.getBcc());
            }
            if (!StringUtils.isEmpty(mail.getTemplateName())) {
                String content = FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(mail.getTemplateName()), mail.getData());
                helper.setText(content, true);
            } else {
                helper.setText(mail.getText());
            }
            if (!CollectionUtils.isEmpty(mail.getAttachment())) {
                Iterator<Map.Entry<String, byte[]>> iterator = mail.getAttachment().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, byte[]> entry = iterator.next();
                    helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue()));
                }
            }
            helper.setPriority(mail.getPriority() == null ? 1 : mail.getPriority());
            sender.send(message);
        }
    }

    /**
     * 将附件保存到ftp服务器，并返回文件在ftp服务器的地址列表
     * @param attachments
     * @return
     * @throws IOException
     *
     * @author 陈宇霖
     * @date 2017年09月21日14:44:34
     */
    private LinkedHashMap<String, String> saveAttachment(LinkedHashMap<String, byte[]> attachments) throws IOException {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        //使用日期将附件分开存放
        String dailyDir = Constants.MAIL_ATTACHMENT_FTP_BASE_DIR + File.separatorChar + DateUtils.convert(new Date(), DateUtils.DATE_SHORT_FORMAT);
        Iterator<String> iterable = attachments.keySet().iterator();
        while (iterable.hasNext()) {
            String name = iterable.next();
            //生成一个id来当做文件名称吧
            String renamedFileName = String.valueOf(idGenerator.nextId()) + (name.lastIndexOf(".") == - 1 ? "" : name.substring(name.lastIndexOf(".")));
            byte[] data = attachments.get(name);
            boolean updateSuccess = ftpUtils.uploadFile(dailyDir, renamedFileName, new ByteArrayInputStream(data));
            if (updateSuccess) {
                result.put(name, dailyDir + File.separator + renamedFileName);
            } else {
                throw new BusinessException("mail.upload.attachment.error");
            }
        }
        return result;
    }

    /**
     * 根据数据库中存储的附件路径，从ftp下载附件
     * @param attachments
     * @return
     * @author 陈宇霖
     * @date 2017年09月22日10:13:42
     */
    private LinkedHashMap<String, byte[]> downloadAttachment(List<MailAttachmentPO> attachments) throws IOException {
        LinkedHashMap<String, byte[]> result = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(attachments)) {
            for (int i = 0; i < attachments.size(); i ++) {
                MailAttachmentPO attachment = attachments.get(i);
                String name = attachment.getAttachmentName();
                String path = attachment.getAttachmentUrl();
                byte[] file = ftpUtils.retrieveFile(path.substring(0, path.lastIndexOf(File.separator)), path.substring(path.lastIndexOf(File.separator) + 1));
                result.put(name, file);
            }
        }
        return result;
    }

    /**
     * 将接口的邮件主信息dto转换成持久化实体
     * @param dto
     * @return
     * @author 陈宇霖
     * @date 2017年09月21日08:20:23
     */
    private MailPO convertMailDomain2Po(Mail dto, boolean sync) {
        MailPO mailPO = new MailPO();
        mailPO.setSendType(dto.getSendType());
        mailPO.setSender(dto.getFrom());
        mailPO.setSubject(dto.getSubject());
        mailPO.setReceiver(convertArrayToString(dto.getTo()));
        mailPO.setCopyTo(convertArrayToString(dto.getCc()));
        mailPO.setBlindCopyTo(convertArrayToString(dto.getBcc()));
        mailPO.setTemplateName(dto.getTemplateName());
        mailPO.setTemplateData(JSON.toJSONString(dto.getData()));
        mailPO.setContent(dto.getText());
        mailPO.setContainsAttachment(CollectionUtils.isEmpty(dto.getAttachment()) ? 0 : 1);
        mailPO.setPriority(dto.getPriority());
        mailPO.setRequiredSendTime(dto.getRequiredSendTime() == null ? new Date() : dto.getRequiredSendTime());
        mailPO.setSendStatus(sync ? Constants.SEND_STATUS_SENDING : Constants.SEND_STATUS_WAIT);
        mailPO.setId(idGenerator.nextId());
        mailPO.setActive(ApplicationConstants.ACTIVE);
        mailPO.setCreateTime(new Date());
        mailPO.setCreateUserCode(ApplicationConstants.SYSTEM_OPERATOR);
        return mailPO;
    }

    /**
     * 将数据库持久化的邮件对象转换成待发送的数据
     * @param mailPO
     * @return
     * @author 陈宇霖
     * @date 2017年09月22日10:04:58
     */
    private Mail convertMailPo2Dto(MailPO mailPO) {
        Mail message = new Mail();
        message.setSendType(mailPO.getSendType());
        message.setFrom(mailPO.getSender());
        message.setSubject(mailPO.getSubject());
        message.setTo(convertStringToArray(mailPO.getReceiver()));
        message.setCc(convertStringToArray(mailPO.getCopyTo()));
        message.setBcc(convertStringToArray(mailPO.getBlindCopyTo()));
        message.setTemplateName(mailPO.getTemplateName());
        message.setData(JSON.parseObject(mailPO.getTemplateData(), Map.class));
        message.setText(mailPO.getContent());
        message.setPriority(mailPO.getPriority());
        message.setRequiredSendTime(mailPO.getRequiredSendTime());
        return message;
    }

    /**
     * 将收件人、抄送人等数组转换成分号分割的字符串
     * @param array
     * @return
     * @author 陈宇霖
     * @date 2017年09月21日08:00:36
     */
    private String convertArrayToString(String[] array) {
        StringBuilder result = new StringBuilder();
        if (array != null && array.length > 0) {
            for (int i = 0; i < array.length; i++) {
                if (i == array.length - 1) {
                    result.append(array[i]);
                } else {
                    result.append(array[i]).append(";");
                }
            }
        }
        return result.toString();
    }

    /**
     * 将收件人、抄送人等以分号分割的字符串转换成数组
     * @param string
     * @return
     * @author 陈宇霖
     * @date 2017年09月22日09:58:09
     */
    private String[] convertStringToArray(String string) {
        if (!StringUtils.isEmpty(string)) {
            return string.split(";");
        }
        return null;
    }

    /**
     * 邮件发送线程
     * @author 陈宇霖
     * @date 2017年09月25日08:28:59
     */
    public class SendMailThread implements Runnable {

        private MailPO mailToSend = null;

        public SendMailThread(MailPO mailToSend) {
            this.mailToSend = mailToSend;
        }

        @Override
        public void run() {
            sendMailAndSaveResult(mailToSend);
        }
    }

    /**
     * 发送邮件并记录发送结果
     * @param mailToSend
     * @author 陈宇霖
     * @date 2017年09月25日13:55:51
     */
    private void sendMailAndSaveResult(MailPO mailToSend) {
        try {
            Mail message = convertMailPo2Dto(mailToSend);
            //如果有附件，需要将附件从ftp中下载下来
            if (mailToSend.getContainsAttachment() == 1) {
                Example example = new Example(MailAttachmentPO.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andCondition("is_active = ", ApplicationConstants.ACTIVE);
                criteria.andCondition("mail_id = ", mailToSend.getId());
                List<MailAttachmentPO> attachments = mailAttachmentDao.selectByExample(example);
                LinkedHashMap<String, byte[]> files = downloadAttachment(attachments);
                message.setAttachment(files);
            }
            sendMail(message);
            MailPO successData = new MailPO();
            successData.setId(mailToSend.getId());
            successData.setSendStatus(Constants.SEND_STATUS_SUCCESS);
            successData.setSendTime(new Date());
            successData.setModifyTime(new Date());
            successData.setModifyUserCode(ApplicationConstants.SYSTEM_OPERATOR);
            mailDao.updateSendSuccess(successData);
        } catch (Exception e) {
            logger.error("MailPO Send Error", e);
            MailPO failure = new MailPO();
            failure.setId(mailToSend.getId());
            failure.setSendStatus(Constants.SEND_STATUS_FAILURE);
            //失败次数+1
            failure.setFailureTimes((mailToSend.getFailureTimes() == null ? 0 : mailToSend.getFailureTimes()) + 1);
            String fullStack = ExceptionUtils.getFullStackTrace(e);
            if (fullStack.length() > 5000) {
                fullStack = fullStack.substring(0, 4800);
            }
            failure.setFailureReason(fullStack);
            failure.setSendTime(new Date());
            failure.setModifyTime(new Date());
            failure.setModifyUserCode(ApplicationConstants.SYSTEM_OPERATOR);
            mailDao.updateSendFailure(failure);
        }
    }

}
