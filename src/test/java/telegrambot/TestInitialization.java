package telegrambot;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Order;
import telegrambot.config.Initialization;
import telegrambot.core.FiapBot;
import telegrambot.i18n.ConfigLocalMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

@Log
public class TestInitialization implements ConfigLocalMessage {

    private Initialization initialization = new Initialization();

    @BeforeAll
    public static void beforeAll() {

    }

    @Test
    public void testLoadConfigFile() {
        log.info("Telegram Token: \t\t" + initialization.getToken());
        log.info("Telegram Updates Limit: \t" + initialization.getUpdatesLimit());
    }

    @Test
    @Order(1)
    public void testMessages_DefaultLocale() {
        Locale.setDefault(Locale.getDefault());
        loadMessagesFile("[01-" + Locale.getDefault());
    }

    @Test
    @Order(2)
    public void testMessages_en_US() {
        Locale.setDefault(new Locale("en", "US"));
        loadMessagesFile("[02-" + Locale.getDefault());
    }

    @Test
    @Order(3)
    public void testMessages_es_ES() {
        Locale.setDefault(new Locale("es", "ES"));
        loadMessagesFile("[03-" + Locale.getDefault());
    }

    public void loadMessagesFile(String seq) {
        log.info(seq + "]---------------------------------------------------------------------------");
        log.info("telegramBot.encoding: \t"         + getTextMessage("telegramBot.encoding"));
        //log.info("return.default: \t"               + getTextMessage("return.default"));
        //log.info("return.greeting.user: \t"         + getTextMessage("return.greeting.user"));
        //log.info("exception.invalid.message: \t"    + getTextMessage("exception.invalid.message"));
        log.info("bot.greeting: \t"                 + getTextMessage("bot.greeting"));
        log.info("bot.unexpected.error: \t"         + getTextMessage("bot.unexpected.error"));
        log.info("bot.joke.message: \t"             + getTextMessage("bot.joke.message"));
        log.info("bot.joke.congrats: \t"            + getTextMessage("bot.joke.congrats"));
        log.info("bot.joke.try_again: \t"           + getTextMessage("bot.joke.try_again"));
        log.info("bot.joke.answer: \t"              + getTextMessage("bot.joke.answer"));
        log.info("bot.see_you: \t"                  + getTextMessage("bot.see_you"));
        log.info("bot_dont_understanding: \t"       + getTextMessage("bot_dont_understanding"));
        // TODO Acrescentar todas as mensagens...
    }

    @Test
    public void testRun() {
        new FiapBot().start();
    }

    @AfterAll
    public static void afterAll() {

    }

}
