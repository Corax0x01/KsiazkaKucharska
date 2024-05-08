package szymanski.jakub.backend.email;

public enum EmailTemplateName {

    CONFIRM_EMAIL("confirm_email"),
    ACTIVATE_ACCOUNT("activate_account")

    ;
    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
