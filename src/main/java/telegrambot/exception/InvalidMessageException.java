package telegrambot.exception;

public class InvalidMessageException extends RuntimeException implements ExceptionKeyMessage {

    public InvalidMessageException() {
        super(EXCEPTION_INVALID_MESSAGE);
    }

    /**
     * {@inheritDoc}
     * <hr />Adiciona suporte a <i>i18n</i> nas mensagens de erro.
     * @return Mensagem de acordo com a língua configurada
     */
    @Override
    public String getLocalizedMessage() {
        return getTextMessage(this.getMessage());
    }
}
