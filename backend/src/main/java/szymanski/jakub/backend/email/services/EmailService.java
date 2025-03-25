package szymanski.jakub.backend.email.services;

import jakarta.mail.MessagingException;
import szymanski.jakub.backend.email.EmailTemplateName;

public interface EmailService {
    /**
     * Sends email.
     *
     * @param to                    email receiver
     * @param username              email sender
     * @param emailTemplate         {@link EmailTemplateName name} of email template to send
     * @param confirmationUrl       URL for confirming account (optional)
     * @param activationCode        code for account activation
     * @param subject               email subject
     * @throws MessagingException   when sending an email fails
     */
    void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject) throws MessagingException;
}
