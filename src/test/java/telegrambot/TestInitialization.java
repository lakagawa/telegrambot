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
        Locale.setDefault(Locale.getDefault()); // pt
        loadMessagesFile("[01-" + Locale.getDefault());
    }

    @Test
    @Order(2)
    public void testMessages_en_US() {
        //Locale.setDefault(new Locale("en", "US"));
        Locale.setDefault(new Locale("en"));
        loadMessagesFile("[02-" + Locale.getDefault());
    }

    @Test
    @Order(3)
    public void testMessages_es_ES() {
        //Locale.setDefault(new Locale("es", "ES"));
        Locale.setDefault(new Locale("es"));
        loadMessagesFile("[03-" + Locale.getDefault());
    }

    public void loadMessagesFile(String seq) {
        log.info(seq + "]---------------------------------------------------------------------------");
        log.info("telegramBot.encoding: \t"             + getTextMessage("telegramBot.encoding"));
        //log.info("bot.exception.invalid.message: \t"    + getTextMessage("bot.exception.invalid.message"));
        log.info("bot.exception.error_unexpected: \t"   + getTextMessage("bot.exception.error_unexpected"));
        log.info("bot.choice.jokes: \t"                 + getTextMessage("bot.choice.jokes"));
        log.info("bot.choice.quit: \t"                  + getTextMessage("bot.choice.quit"));
        log.info("bot.choice.negative: \t"              + getTextMessage("bot.choice.negative"));
        log.info("bot.choice.positive: \t"              + getTextMessage("bot.choice.positive"));
        log.info("bot.choice.later: \t"                 + getTextMessage("bot.choice.later"));
        log.info("bot.choice.language: \t"              + getTextMessage("bot.choice.language"));
        log.info("bot.choice.language.options: \t"      + getTextMessage("bot.choice.language.options"));
        log.info("bot.message.greeting: \t"             + getTextMessage("bot.message.greeting"));
        //log.info("bot.message.greeting.morning: \t"     + getTextMessage("bot.message.greeting.morning"));
        //log.info("bot.message.greeting.afternoon: \t"   + getTextMessage("bot.message.greeting.afternoon"));
        //log.info("bot.message.greeting.night: \t"       + getTextMessage("bot.message.greeting.night"));
        //log.info("bot.message.greeting.language: \t"    + getTextMessage("bot.message.greeting.language"));
        log.info("bot.message.joke: \t"                 + getTextMessage("bot.message.joke"));
        log.info("bot.message.joke.congrats: \t"        + getTextMessage("bot.message.joke.congrats"));
        log.info("bot.message.joke.try_again: \t"       + getTextMessage("bot.message.joke.try_again"));
        log.info("bot.message.joke.answer: \t"          + getTextMessage("bot.message.joke.answer"));
        log.info("bot.message.see_you: \t"              + getTextMessage("bot.message.see_you"));
        log.info("bot.message.dont_understanding: \t"   + getTextMessage("bot.message.dont_understanding"));
        log.info("bot.message.maybe_later: \t"          + getTextMessage("bot.message.maybe_later"));
    }

    @Test
    public void testRun() {
        //new FiapBot().start();
    }

    @AfterAll
    public static void afterAll() {

    }

}
