package telegrambot.i18n;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Possibilita suporte a i18n
 * @author Thiago Zanella
 */
public interface ConfigLocalMessage {

    /**
     * Obtém a mensagem de acordo com a localização/idioma configurado
     * @param value Chave da mensagem
     * @return Mensagem conforme o idioma
     */
    default String getTextMessage(String value) {
        try {
            ResourceBundle message = ResourceBundle.getBundle("message", Locale.getDefault());
            return new String(message.getString(value).getBytes(), message.getString("telegramBot.encoding"));
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

}
