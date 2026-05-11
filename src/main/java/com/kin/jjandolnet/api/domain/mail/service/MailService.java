package com.kin.jjandolnet.api.domain.mail.service;

import com.kin.jjandolnet.api.domain.user.entity.User;
import com.kin.jjandolnet.api.domain.user.repository.UserRepository;
import com.kin.jjandolnet.global.error.exception.BusinessException;
import com.kin.jjandolnet.global.error.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendTempPw(Map<String, String> request) {

        String toEmail = request.get("email");

        User user = userRepository.findByEmail(toEmail)
                .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("짠돌넷 임시 비밀번호 입니다.");
            helper.setFrom("kin412@naver.com"); // yml의 username과 동일해야 함

            String htmlContent = createTempPwHtml(tempPassword);
            helper.setText(htmlContent, true); // true를 설정해야 HTML로 인식

            javaMailSender.send(message);

            String encodedPassword = passwordEncoder.encode(tempPassword);
            user.updateUser(encodedPassword);
        }catch (MessagingException e){
            throw new RuntimeException("메일 발송 중 오류가 발생했습니다.");
        }
    }

    private String createTempPwHtml(String tempPassword) {
        return """
        <div style="font-family: 'Apple SD Gothic Neo', 'Malgun Gothic', sans-serif; width: 100%%; max-width: 600px; margin: 0 auto; border: 1px solid #eee; border-radius: 10px; overflow: hidden;">
            <div style="background-color: #FF9F1C; padding: 20px; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">💰 짠돌넷에서 알려드려요.</h1>
            </div>
            <div style="padding: 30px; line-height: 1.6; color: #333;">
                <h2 style="color: #FF9F1C; margin-top: 0;">임시 비밀번호 안내</h2>
                <p>안녕하세요! 절약의 즐거움을 함께하는 <b>짠돌넷</b>입니다.</p>
                <p>요청하신 임시 비밀번호를 발송해 드립니다.</p>
                
                <div style="background-color: #F8F9FA; border-radius: 8px; padding: 20px; text-align: center; margin: 25px 0; border: 1px dashed #FF9F1C;">
                    <span style="font-size: 14px; color: #666; display: block; margin-bottom: 10px;">임시 비밀번호</span>
                    <strong style="font-size: 28px; color: #E71D36; letter-spacing: 2px;">%s</strong>
                </div>
                
                <p style="font-size: 14px; color: #666;">
                    ※ 로그인 후 [내 정보] 메뉴에서 반드시 새 비밀번호로 변경해 주세요.<br>
                </p>
                
                <div style="text-align: center; margin-top: 30px;">
                    <a href="http://localhost:5173/login" style="background-color: #FF9F1C; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;">짠돌넷 바로가기</a>
                </div>
            </div>
            <div style="background-color: #f4f4f4; padding: 15px; text-align: center; font-size: 12px; color: #999;">
                © 2026 JJandol-Net. All rights reserved.
            </div>
        </div>
        """.formatted(tempPassword);
    }
}
