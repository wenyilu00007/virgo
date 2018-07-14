package com.wyl.virgo.mail;

import com.alibaba.fastjson.JSON;
import com.wyl.virgo.mail.shared.Constants;
import com.wyl.zodiac.web.proxy.virgo.dto.MailEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Title: MailServiceTest
 * @Description: 邮件发送服务测试
 * @author
 * @date 2017/9/19 16:02
 * @version V1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VirgoSendMailApplication.class)
@WebAppConfiguration
public class MailServiceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    /**
     *获取指定用户在指定系统所拥有的菜单权限列表 带语言编码
     * @params:[]
     * @return:void
     * @author:wyl
     * @date 2017/9/8 14:21
     */
    @Test
    public void sendSimpleMail() throws Exception {
        MailEntity messageDTO = new MailEntity();
        messageDTO.setFrom("yulin.chen@itiaoling.com");
        messageDTO.setSubject("Simple Email Test");
        messageDTO.setTo(new String[]{"dingyong@hoau.net", "zhenzhen.guo@itiaoling.com"});
        messageDTO.setCc(new String[]{"deyun.liu@itiaoling.com", "yilu.wen@itiaoling.com"});
        messageDTO.setBcc(new String[]{"guoxing.qiu@itiaoling.com", "long.xu@itiaoling.com"});
        messageDTO.setText("This is a test email");
        messageDTO.setSendType(Constants.SEND_TYPE_SYNC);
        String requestUrl= "/mails/v1/mail/sync";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(messageDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(response.getContentAsString().contains("\"success\":true"),true);
    }

    /**
     *获取指定用户在指定系统所拥有的菜单权限列表 带语言编码
     * @params:[]
     * @return:void
     * @author:wyl
     * @date 2017/9/8 14:21
     */
    @Test
    public void sendTemplateMail() throws Exception {
        MailEntity messageDTO = new MailEntity();
        messageDTO.setFrom("yulin.chen@itiaoling.com");
        messageDTO.setSubject("Simple Email Test");
        messageDTO.setTo(new String[]{"dingyong@hoau.net"});
        messageDTO.setTemplateName("TestTemplate.ftl");
        Map data = new HashMap();
        data.put("user", "丁勇");
        data.put("address", "http://tuiguang.189auto.com.cn/2017/kaixueji");
        messageDTO.setSendType(Constants.SEND_TYPE_SYNC);
        messageDTO.setData(data);
        String requestUrl= "/mails/v1/mail/sync";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(messageDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(response.getContentAsString().contains("\"success\":true"),true);
    }

    /**
     *获取指定用户在指定系统所拥有的菜单权限列表 带语言编码
     * @params:[]
     * @return:void
     * @author:wyl
     * @date 2017/9/8 14:21
     */
    @Test
    public void sendAttachmentMail() throws Exception {
        MailEntity messageDTO = new MailEntity();
        messageDTO.setFrom("yulin.chen@itiaoling.com");
        messageDTO.setSubject("Simple Email Test");
        messageDTO.setTo(new String[]{"yulin.chen@itiaoling.com"});
        messageDTO.setTemplateName("TestTemplate.ftl");
        Map data = new HashMap();
        data.put("user", "丁勇");
        data.put("address", "http://tuiguang.189auto.com.cn/2017/kaixueji");
        messageDTO.setSendType(Constants.SEND_TYPE_SYNC);
        messageDTO.setData(data);
        byte[] attachment1 = StreamUtils.copyToByteArray(new FileInputStream("/Users/Sai/Desktop/需求编号595_物流单同步_V1.3.xlsx"));
        LinkedHashMap attachments = new LinkedHashMap();
        attachments.put("需求编号595_物流单同步_V1.3.xlsx", attachment1);
        messageDTO.setAttachment(attachments);
        String requestUrl= "/mails/v1/mail/sync";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(messageDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(response.getContentAsString().contains("\"success\":true"),true);
    }

    @Test
    public void sendAsyncMail() throws Exception {
        for (int i = 0; i < 100; i++) {
            MailEntity messageDTO = new MailEntity();
            messageDTO.setSendType(Constants.SEND_TYPE_ASYNC);
            messageDTO.setFrom("yulin.chen@itiaoling.com");
            messageDTO.setSubject("Simple Email Test");
            messageDTO.setTo(new String[]{"yulin.chen@itiaoling.com"});
            messageDTO.setTemplateName("TestTemplate.ftl");
            Map data = new HashMap();
            data.put("user", "Sai");
            data.put("address", "http://tuiguang.189auto.com.cn/2017/kaixueji");
            messageDTO.setSendType(Constants.SEND_TYPE_SYNC);
            messageDTO.setData(data);
            byte[] attachment1 = StreamUtils.copyToByteArray(new FileInputStream("/opt/Test/TestMailAttachment.txt"));
            LinkedHashMap attachments = new LinkedHashMap();
            attachments.put("TestMailAttachment.txt", attachment1);
            messageDTO.setAttachment(attachments);
            String requestUrl= "/mails/v1/mail/async";
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(JSON.toJSONString(messageDTO))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            Assert.assertEquals(response.getContentAsString().contains("\"success\":true"),true);
        }
    }

    @Test
    public void scheduleSendMail() throws Exception {
        String requestUrl= "/mails/v1/mail/actions/send";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(requestUrl)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assert.assertEquals(response.getContentAsString().contains("\"success\":true"),true);
    }

}
