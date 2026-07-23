package util;

import exception.InvalidAgeException;
import exception.InvalidAmountException;
import exception.InvalidPhoneException;
import model.Account;

import java.util.regex.Pattern;

/**
 * Stateless utility class that centralises all input-validation logic.
 * <p>
 * Demonstrates: Static Members (all methods are static), Encapsulation (private constructor),
 * Exception Handling (throws custom exceptions), String Handling, Regex.
 * </p>
 */
public final class Validation {

    // ------------------------------------------------------------------
    // Constants / compiled patterns
    // ------------------------------------------------------------------

    /** Pattern that matches a 10-digit mobile number (no spaces or dashes). */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

    /** Simple pattern to validate that an email address has an @ and a domain. */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // ------------------------------------------------------------------
    // Private constructor
    // ------------------------------------------------------------------

    /**
     * Private constructor prevents instantiation.
     * All validation methods are static; no object is ever needed.
     */
    private Validation() {
        throw new UnsupportedOperationException("Utility class — do not instantiate.");
    }

    // ------------------------------------------------------------------
    // Amount validation
    // ------------------------------------------------------------------

    /**
     * Validates that a monetary amount is strictly positive.
     * Demonstrates: Custom exception throwing.
     *
     * @param amount the value to validate
     * @throws InvalidAmountException if the amount is zero or negative
     */
    public static void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Amount must be greater than zero. Received: " + amount, amount);
        }
    }

    /**
     * Validates that a monetary amount is not negative (zero is allowed for balance display).
     *
     * @param amount the value to validate
     * @throws InvalidAmountException if the amount is negative
     */
    public static void validateNonNegativeAmount(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(
                    "Amount cannot be negative. Received: " + amount, amount);
        }
    }

    // ------------------------------------------------------------------
    // Age validation
    // ------------------------------------------------------------------

    /**
     * Validates that an age is within the acceptable range for account creation.
     * Demonstrates: Custom exception, constants from another class.
     *
     * @param age the age to validate
     * @throws InvalidAgeException if age is outside [MIN_AGE, MAX_AGE]
     */
    public static void validateAge(int age) throws InvalidAgeException {
        if (age < Account.MIN_AGE || age > Account.MAX_AGE) {
            throw new InvalidAgeException(
                    String.format("Age must be between %d and %d. Received: %d",
                                  Account.MIN_AGE, Account.MAX_AGE, age), age);
        }
    }

    // ------------------------------------------------------------------
    // Phone validation
    // ------------------------------------------------------------------

    /**
     * Validates that a phone number is a valid 10-digit Indian mobile number.
     * Indian mobile numbers begin with 6, 7, 8, or 9.
     * Demonstrates: String handling, Regex, Custom exceptions.
     *
     * @param phone the phone number string to validate
     * @throws InvalidPhoneException if the phone number format is invalid
     */
    public static void validatePhone(String phone) throws InvalidPhoneException {
        if (phone == null || phone.isBlank()) {
            throw new InvalidPhoneException("Phone number cannot be blank.", phone);
        }
        String cleaned = phone.trim();
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new InvalidPhoneException(
                    "Invalid phone number. Must be 10 digits starting with 6-9. Received: " + cleaned,
                    cleaned);
        }
    }

    // ------------------------------------------------------------------
    // Email validation
    // ------------------------------------------------------------------

    /**
     * Validates that an e-mail address matches a basic format.
     *
     * @param email the e-mail string to validate
     * @throws IllegalArgumentException if the e-mail is blank or malformed
     */
    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be blank.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException(
                    "Invalid email address format: " + email);
        }
    }

    // ------------------------------------------------------------------
    // Name / string validation
    // ------------------------------------------------------------------

    /**
     * Validates that a name is not blank and contains only alphabetical characters and spaces.
     *
     * @param name  the name to validate
     * @param field the field label used in the error message (e.g. "Customer name")
     * @throws IllegalArgumentException if the name is blank or contains invalid characters
     */
    public static void validateName(String name, String field) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank.");
        }
        if (!name.trim().matches("[A-Za-z ]+")) {
            throw new IllegalArgumentException(
                    field + " must contain only letters and spaces. Received: " + name);
        }
    }

    // ------------------------------------------------------------------
    // Generic not-blank check
    // ------------------------------------------------------------------

    /**
     * Ensures a string is not null or blank.
     *
     * @param value the value to check
     * @param field the field label used in the error message
     * @throws IllegalArgumentException if the value is blank
     */
    public static void validateNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank.");
        }
    }

    // ------------------------------------------------------------------
    // Convenience boolean checks (no exception — for conditional logic)
    // ------------------------------------------------------------------

    /**
     * Returns {@code true} if the phone number passes validation without throwing.
     *
     * @param phone the phone number to check
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Returns {@code true} if the email passes validation without throwing.
     *
     * @param email the email to check
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Returns {@code true} if the amount is strictly positive.
     *
     * @param amount the amount to check
     * @return true if amount &gt; 0
     */
    public static boolean isPositiveAmount(double amount) {
        return amount > 0;
    }
}
