package com.xyw.spbodemo.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;


@Service
public class MailSender implements InitializingBean {

    @Autowired
    private SpringTemplateEngine thymeleafEngine;

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    public boolean sendWithHtmlTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("xxx");
            InternetAddress from = new InternetAddress("1641137052@qq.com");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //add the context
            Context context = new Context();
            if (model != null) {
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            }

            String result = thymeleafEngine.process(template, context);

            //String result = VelocityEngineUtils
            //      .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;

        } catch (Exception e) {
            logger.error("failed to send mail " + e.getMessage());
            return false;
        }

    }


    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        //input your own mail and password to send mail
        mailSender.setUsername("1641137052@qq.com");
        mailSender.setPassword("iyrhzbindkxnbgch");
        mailSender.setHost("smtp.qq.com");

        mailSender.setPort(465);
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);

    }
}
