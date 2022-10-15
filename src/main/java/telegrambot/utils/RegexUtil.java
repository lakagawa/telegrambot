package telegrambot.utils;

public final class RegexUtil {

    public static String getRegexCaseInsensitive(String value) {
        return "(?i).*" +  value + ".*";
    }

    public static String getRegexMatchLanguague() {
        return "(\\bes\\b|\\bpt\\b|\\ben\\b).*";
    }
}
