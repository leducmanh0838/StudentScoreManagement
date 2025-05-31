package com.ldm.services;

import com.ldm.dto.ScoreAndCriteriaDTO;
import com.ldm.dto.StudentGradeMailDTO;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private String buildGradeHtml(StudentGradeMailDTO dto) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Grades</title></head><body>");
        html.append("<h2>Dear Student,</h2>");
        html.append("<p>Here are your grades for the course:</p>");
        html.append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse;'>");
        html.append("<thead><tr><th>Criteria</th><th>Score</th><th>Weight (%)</th></tr></thead><tbody>");

        double total = 0.0;
        int totalWeight=0;

        for (ScoreAndCriteriaDTO score : dto.getScores()) {
            html.append("<tr>");
            html.append("<td>").append(score.getCriteria() != null ? score.getCriteria() : "N/A").append("</td>");
            html.append("<td>").append(score.getScore() != null ? score.getScore() : "N/A").append("</td>");
            html.append("<td>").append(score.getWeight() != null ? score.getWeight() : "N/A").append("</td>");
            html.append("</tr>");

            // Tính tổng điểm nếu dữ liệu hợp lệ
            if (score.getScore() != null && score.getWeight() != null) {
                total += score.getScore() * score.getWeight() / 100.0;
                totalWeight+=score.getWeight();
            }
        }

        html.append("<tr style='font-weight:bold; background-color:#f0f0f0;'>");
        html.append("<td>Total Grade</td>");
        html.append("<td>").append(String.format("%.2f", total)).append("</td>");
        html.append("<td>").append(String.format("%d%%", totalWeight)).append("</td>");
        html.append("</tr>");

        html.append("</tbody></table>");
        html.append("<p>Best regards,<br>OU</p>");
        html.append("</body></html>");

        return html.toString();
    }

    @Async
    public void sendGradesToStudents(List<StudentGradeMailDTO> students, String courseName) {

        for (StudentGradeMailDTO student : students) {
            String email = student.getStudentEmail();
            String subject = String.format("%s Subject Score Announcement", courseName);
            String body = this.buildGradeHtml(student);

//            emailService.sendHtmlEmail(email, subject, body);
            sendHtmlEmail(email, subject, body);
        }
    }

    @Autowired
    private JavaMailSender mailSender;

    private String buildVerificationHtml(String otp) {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verify Your Email</title>
        </head>
        <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px;">
            <div style="background: linear-gradient(to right, #4CAF50, #45a049); padding: 20px; text-align: center;">
                <h1 style="color: white; margin: 0;">Verify Your Email</h1>
            </div>
            <div style="background-color: #f9f9f9; padding: 20px; border-radius: 0 0 5px 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                <p>Hello,</p>
                <p>Thank you for registering! Your verification code is:</p>
                <div style="text-align: center; margin: 30px 0;">
                    <span style="font-size: 32px; font-weight: bold; letter-spacing: 5px; color: #4CAF50;">%s</span>
                </div>
                <p>Enter this code on the verification page to complete your registration.</p>
                <p>This code will expire in 15 minutes for security reasons.</p>
                <p>If you did not create an account with us, please ignore this email.</p>
                <p>Best regards,<br>OU</p>
            </div>
            <div style="text-align: center; margin-top: 20px; color: #888; font-size: 0.8em;">
                <p>This is an automated message, please do not reply to this email.</p>
            </div>
        </body>
        </html>
        """.formatted(otp);
    }

    @Async
    public void sendVerificationEmail(String to, String otp) {
        // Template email
        String emailBody = buildVerificationHtml(otp);

        // Gửi email
        sendHtmlEmail(to, "Verify Your Email", emailBody);
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("leducmanhmanh2004@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);  // email HTML

            helper.setFrom("leducmanhmanh2004@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // true email HTML

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
