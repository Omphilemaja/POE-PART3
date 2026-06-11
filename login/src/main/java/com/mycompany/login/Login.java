package com.mycompany.login;

public class Login {

    private String storedUsername;
    private String storedPassword;

    // Register user with validation
    public String registerUser(String username, String password, String phone) {

        if (!checkUsername(username)) {
            return "Username is not correctly formatted. Must be at least 5 characters and contain only letters/numbers.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password must be at least 8 characters long, include uppercase, lowercase, digit, and special character.";
        }

        if (!checkCellphoneNumber(phone)) {
            return "Cell phone number incorrectly formatted. Must start with +27 and contain 12 digits.";
        }

        storedUsername = username;
        storedPassword = password; 

        return "Registration successful!";
    }

    // Login check (Fixes the NullPointerException)
    public boolean loginUser(String username, String password) {
        // Guard clause: if either parameter is null, immediately reject the login safely
        if (username == null || password == null) {
            return false;
        }
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    // Phone validation: must start with +27 and be exactly 12 characters
    public boolean checkCellphoneNumber(String phone) {
        if (phone == null || !phone.startsWith("+27")) return false;
        if (phone.length() != 12) return false;

        // Ensure remaining characters are digits
        for (int i = 3; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Username validation: min 5 chars, only letters/numbers
    public boolean checkUsername(String username) {
        if (username == null || username.length() < 5) return false;
        return username.matches("[A-Za-z0-9]+");
    }

    // Password validation: min 8 chars, must include uppercase, lowercase, digit, special char
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true; // Any character that isn't a letter/digit acts as special
        }

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
