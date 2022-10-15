package telegrambot.utils;

/**
 * Disponibiliza tratamento de textos usando RegEx
 * @author Lais Kagawa
 */
public final class RegexUtil {

    /**
     * Desconsiderada a caixa do texto nas comparações
     * @param value Texto que será incluído na regra RegEx
     * @return Valor formatado com a regra RegEx
     */
    public static String getRegexCaseInsensitive(String value) {
        return "(?i).*" +  value + ".*";    // TODO Avaliar se faz sentido implementar considerando i18n
        //return String.format(value, getTextMessage("bot.regex.insensitive")); // bot.regex.insensitive=(?i).*%s.*
    }

    /**
     * Obtém o padrão RegEx para verificar a formatação da mensagem durante a sua escolha
     * @return Padrão de texto esperado para configurar a linguagem
     */
    public static String getRegexMatchLanguague() {
        return "(\\bes\\b|\\bpt\\b|\\ben\\b).*";    // TODO Avaliar implementar considerando i18n
        //return getTextMessage("bot.regex.match_language"); // bot.regex.match_language=(\bes\b|\bpt\b|\ben\b).*
    }

}
