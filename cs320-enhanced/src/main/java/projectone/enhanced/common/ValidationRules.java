package projectone.enhanced.common;

/*
 * Validation rules from the original stored in one place instead of magic numbered in every time.
 */
public final class ValidationRules {

    public static final int ID_MAX = 10;
    public static final int CONTACT_NAME_MAX = 10;
    public static final String PHONE_PATTERN = "\\d{10}";
    public static final int ADDRESS_MAX = 30;
    public static final int TASK_NAME_MAX = 20;
    public static final int DESCRIPTION_MAX = 50;

    public static final String USERNAME_PATTERN = "[A-Za-z0-9_]{3,20}";
    public static final int PASSWORD_MIN = 8;
    // BCrypt only uses the first 72 bytes of a password
    public static final int PASSWORD_MAX = 72;

    private ValidationRules() {
    }
}
