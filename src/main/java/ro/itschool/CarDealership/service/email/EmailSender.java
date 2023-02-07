package ro.itschool.CarDealership.service.email;

public interface EmailSender {
    void sendEmail(String toEmail, String subject, String body);
}