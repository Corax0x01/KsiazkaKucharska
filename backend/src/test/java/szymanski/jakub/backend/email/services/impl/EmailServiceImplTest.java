package szymanski.jakub.backend.email.services.impl;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import szymanski.jakub.backend.email.EmailTemplateName;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;
    private EmailServiceImpl emailService;
    private MimeMessage message;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        templateEngine = mock(SpringTemplateEngine.class);
        message = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(message);
        emailService = new EmailServiceImpl(mailSender, templateEngine);
    }

    @Test
    void sendsHtmlEmailUsingRequestedTemplateAndContext() throws Exception {
        when(templateEngine.process(eq("activate_account"), any(Context.class))).thenReturn("<p>Activate</p>");

        emailService.sendEmail("chef@example.com", "Chef", EmailTemplateName.ACTIVATE_ACCOUNT,
                "https://example.test/activate", "123456", "Activate account");

        verify(templateEngine).process(eq("activate_account"), argThat(context ->
                "Chef".equals(context.getVariable("username"))
                        && "https://example.test/activate".equals(context.getVariable("confirmation_url"))
                        && "123456".equals(context.getVariable("activation_code"))));
        verify(mailSender).send(message);
        assertEquals("chef@example.com", message.getAllRecipients()[0].toString());
        assertEquals("Activate account", message.getSubject());
        assertEquals("contact@ksiazkakucharska.com", message.getFrom()[0].toString());
    }

    @Test
    void usesDefaultTemplateWhenNoTemplateIsProvided() throws Exception {
        when(templateEngine.process(eq("confirm_email"), any(Context.class))).thenReturn("Confirmation");

        emailService.sendEmail("chef@example.com", "Chef", null, "https://example.test",
                "123456", "Confirm email");

        verify(templateEngine).process(eq("confirm_email"), any(Context.class));
        verify(mailSender).send(message);
    }
}
