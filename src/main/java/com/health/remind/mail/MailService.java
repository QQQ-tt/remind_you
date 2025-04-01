package com.health.remind.mail;

import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author QQQtx
 * @since 2025/3/27
 */
@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String name;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void test() throws Exception {
        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        // 创建 MimeMessageHelper
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        // 发件人邮箱和名称
        helper.setFrom(name, "QQQtx");
        // 收件人邮箱
        helper.setTo("qiantingxu110@gmail.com");
//        helper.setTo("592600187@qq.com");
        // 邮件标题
        helper.setSubject("Hello");
        // 邮件正文，第二个参数表示是否是HTML正文
        helper.setText("Hello <strong> World</strong>！", true);
        // 发送
        javaMailSender.send(message);
    }

    @SneakyThrows
    public void send(String to, String subject, String text) {
        log.debug("发送邮件:{},{},{}", to, subject, text);
        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();
        // 创建 MimeMessageHelper
        MimeMessageHelper helper = new MimeMessageHelper(message, false);
        // 发件人邮箱和名称
        helper.setFrom(name, "Remind");
        // 收件人邮箱
        helper.setTo(to);
        // 邮件标题
        helper.setSubject(subject);
        // 邮件正文，第二个参数表示是否是HTML正文
        helper.setText(text, true);
        // 发送
        javaMailSender.send(message);
    }
}
