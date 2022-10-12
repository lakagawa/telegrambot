package telegrambot.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public interface ConfigLocalMessage {

    /**
     * Obtém a mensagem de acordo com a localização/idioma configurado
     * @param value Chave da mensagem
     * @return Mensagem conforme o idioma
     */
    default String getTextMessage(String value) {
        try {
            ResourceBundle MESSAGE = ResourceBundle.getBundle("message", Locale.getDefault());
            return new String(MESSAGE.getString(value).getBytes(MESSAGE.getString("telegramBot.encoding")));
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
