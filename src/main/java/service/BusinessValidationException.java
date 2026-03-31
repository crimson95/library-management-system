package service;

/**
 * Domain-level validation exception.
 */
public class BusinessValidationException extends Exception {
    /**
     * Creates a validation exception with a message.
     *
     * @param message validation message
     */
    public BusinessValidationException(String message) {
        super(message);
    }
}
