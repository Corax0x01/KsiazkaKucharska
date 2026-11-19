package szymanski.jakub.backend.email;

import lombok.Getter;

/**
 * Email templates
 */
@Getter
public enum EmailTemplateName {

    CONFIRM_EMAIL("confirm_email"),
    ACTIVATE_ACCOUNT("activate_account")
    ;

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
