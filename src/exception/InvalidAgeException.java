package exception;

/**
 * Custom exception thrown when an invalid age is provided during account creation.
 * Demonstrates: Custom Exception, Inheritance (extends Exception).
 */
public class InvalidAgeException extends Exception {

    /** The invalid age value that triggered this exception. */
    private final int age;

    /**
     * Constructs an InvalidAgeException with only a message.
     *
     * @param message descriptive error message
     */
    public InvalidAgeException(String message) {
        super(message);
        this.age = -1;
    }

    /**
     * Constructs an InvalidAgeException with a message and the invalid age.
     *
     * @param message descriptive error message
     * @param age     the age value that failed validation
     */
    public InvalidAgeException(String message, int age) {
        super(message);
        this.age = age;
    }

    /**
     * Returns the invalid age value.
     *
     * @return the age that caused validation to fail
     */
    public int getAge() {
        return age;
    }
}
