package com.hoau.virgo.mail.controller;

import com.hoau.virgo.mail.shared.domain.Mail;
import com.hoau.virgo.mail.shared.domain.MailSendResult;
import com.hoau.virgo.mail.service.SenderMailService;
import com.hoau.zodiac.core.entity.MailEntity;
import com.hoau.zodiac.core.exception.BusinessException;
import com.hoau.zodiac.web.controller.BasicController;
import com.hoau.zodiac.web.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
* @Title: SendMailController 
* @Package com.hoau.virgo.mail.controller 
* @Description: 邮件发送服务
* @author 陈宇霖  
* @date 2017/9/19 09:22
* @version V1.0   
*/
@RestController
@RequestMapping(value = "/mails")
@Api(value = "/mails", description = "邮件发送服务")
public class SendMailController extends BasicController {

    @Autowired
    private SenderMailService senderMailService;
    /**
     * 即时发送邮件
     *
     * @param message
     * @author 陈宇霖
     * @date 2017年09月19日08:26:24
     */
    @ApiOperation(value = "即时发送邮件", notes = "即时发送邮件")
    @PostMapping("/v1/mail/sync")
    Response sendMailSync(@Valid @NotNull @RequestBody MailEntity message) {
        validateParams(message);
        MailSendResult result = senderMailService.sendMailSync(convertMailDto2Domain(message));
        return returnSuccess(result);
    }

    /**
     * 异步发送邮件
     *
     * @param message
     * @author 陈宇霖
     * @date 2017年09月19日17:04:52
     */
    @ApiOperation(value = "异步发送邮件", notes = "异步发送邮件")
    @PostMapping("/v1/mail/async")
    Response sendMailAsync(@Valid @NotNull @RequestBody MailEntity message) {
        validateParams(message);
        senderMailService.sendMailAsync(convertMailDto2Domain(message));
        return returnSuccess();
    }

    /**
     * 定时任务触发的发送邮件
     *
     * @author 陈宇霖
     * @date 2017年09月19日16:21:25
     */
    @ApiOperation(value = "定时任务触发的发送邮件", notes = "定时任务触发的发送邮件")
    @PutMapping("/v1/mail/actions/send")
    Response scheduleSendMail() {
        senderMailService.scheduleSendMail();
        return returnSuccess();
    }

    /**
     * 参数校验
     * @param params
     * @return
     * @author  陈宇霖
     * @date 2017年09月21日09:14:16
     */
    private void validateParams(MailEntity params) {
        if (params.getTo() == null || params.getTo().length == 0) {
            throw new BusinessException("validator.mail.to.NotNull");
        }
        if (StringUtils.isEmpty(params.getTemplateName()) && StringUtils.isEmpty(params.getText())) {
            throw new BusinessException("validator.mail.content.NotNull");
        }
    }

    /**
     * 将dto对象转换成domain对象
     * @param dto
     * @return
     * @author 陈宇霖
     * @date 2017年09月22日16:25:57
     */
    private Mail convertMailDto2Domain(MailEntity dto) {
        Mail mail = new Mail();
        BeanUtils.copyProperties(dto, mail);
        return mail;
    }

}
