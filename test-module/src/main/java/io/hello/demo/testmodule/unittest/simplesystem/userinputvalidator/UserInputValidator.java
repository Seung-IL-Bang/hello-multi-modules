package io.hello.demo.testmodule.unittest.simplesystem.userinputvalidator;

import java.util.regex.Pattern;

public class UserInputValidator {

    private static final int MAX_NAME_LENGTH = 50;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣a-zA-Z\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() <= MAX_NAME_LENGTH && NAME_PATTERN.matcher(name).matches();
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
