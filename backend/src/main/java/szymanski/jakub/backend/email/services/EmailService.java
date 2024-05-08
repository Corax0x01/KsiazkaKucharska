package szymanski.jakub.backend.email.services;

import jakarta.mail.MessagingException;
import szymanski.jakub.backend.email.EmailTemplateName;

public interface EmailService {
    void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject) throws MessagingException;
}
