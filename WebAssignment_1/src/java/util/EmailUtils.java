/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author an0other
 */
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.OrderItemDTO;
import java.util.List;

public class EmailUtils {

    public static void sendVerificationEmail(String toEmail, String verifyCode, String username) throws MessagingException {
        String fromEmail = "mran0other@gmail.com";
        String password = "awxs ewra nzjx cfyo";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        String subject = "MiniStore: Xác nhận đăng ký tài khoản ";
        try {
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        String link = "/WebAssignment_1/MainController?action=verifyEmail&code=" + verifyCode;
        String content = "<p>Xin chào,</p>"
                + "<p>Nhấn vào liên kết dưới đây để xác nhận đăng ký tài khoản</p>" + username + "<br>"
                + "<a href=\"http://localhost:8080/WebAssignment_1/MainController?action=verifyEmail&code=" + verifyCode + "\">Xác nhận tài khoản</a>"
                + "<p>Trân trọng!</p>";

        message.setContent(content, "text/html; charset=UTF-8");
        Transport.send(message);
    }

    public static void sendForgetPassword(String toEmail, String verifyCode) throws MessagingException {
        String fromEmail = "mran0other@gmail.com";
        String password = "awxs ewra nzjx cfyo";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        String subject = "Ministore: Khôi phục mật khẩu";
        try {
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }

        String content = "<p>Xin chào,</p>"
                + "<p>Nhấn vào liên kết dưới đây để xác nhận khôi phục mật khẩu</p>"
                + "<a href=\"http://localhost:8080/WebAssignment_1/MainController?action=verifyForgetPassword&code=" + verifyCode + "\">Xác nhận khôi phục mật khẩu</a>"
                + "<p>Trân trọng!</p>";

        message.setContent(content, "text/html; charset=UTF-8");
        Transport.send(message);
    }

    public static void sendOrderEmail(String toEmail, int orderId, LocalDateTime orderTime, List<OrderItemDTO> cartList, double totalPrice) throws MessagingException {
        String fromEmail = "mran0other@gmail.com";
        String password = "awxs ewra nzjx cfyo";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        String subject = "MiniStore - Thông tin đơn hàng #" + String.valueOf(orderId);
        try {
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Format ngày
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = orderTime.format(formatter);

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<html><body>");
        emailContent.append("<h2>Cảm ơn bạn đã đặt hàng tại MiniStore!</h2>");
        emailContent.append("<p><strong>Mã đơn hàng:</strong> ").append(String.valueOf(orderId)).append("</p>");
        emailContent.append("<p><strong>Ngày đặt:</strong> ").append(formattedTime).append("</p>");
        emailContent.append("<table style='width:100%; border-collapse: collapse; margin-top: 15px;'>");
        emailContent.append("<thead>");
        emailContent.append("<tr style='background-color:#f2f2f2;'>");
        emailContent.append("<th style='border: 1px solid #ddd; padding: 8px;'>Sản phẩm</th>");
        emailContent.append("<th style='border: 1px solid #ddd; padding: 8px;'>Số lượng</th>");
        emailContent.append("<th style='border: 1px solid #ddd; padding: 8px;'>Giá</th>");
        emailContent.append("</tr>");
        emailContent.append("</thead><tbody>");

        for (OrderItemDTO item : cartList) {
            double itemTotal = item.getPrice() * item.getQuantity();
            emailContent.append("<tr>");
            emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getProduct_name()).append("</td>");
            emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(item.getQuantity()).append("</td>");
            emailContent.append("<td style='border: 1px solid #ddd; padding: 8px;'>$").append(String.format("%.2f", itemTotal)).append("</td>");
            emailContent.append("</tr>");
        }

        emailContent.append("</tbody></table>");
        emailContent.append("<p style='margin-top:15px;'><strong>Tổng cộng: $").append(String.format("%.2f", totalPrice)).append("</strong></p>");
        emailContent.append("<p>Chúng tôi sẽ xử lý và giao hàng sớm nhất cho bạn.</p>");
        emailContent.append("<p>Trân trọng,<br>MiniStore Team</p>");
        emailContent.append("</body></html>");

        message.setContent(emailContent.toString(), "text/html; charset=UTF-8");
        Transport.send(message);
    }
}
