package telegrambot.exception;

public class InvalidMessageException extends RuntimeException implements ExceptionKeyMessage {

    public InvalidMessageException() {
        super(EXCEPTION_INVALID_MESSAGE);
    }

    @Override
    public String getLocalizedMessage() {
        return getTextMessage(this.getMessage());
    }
}
