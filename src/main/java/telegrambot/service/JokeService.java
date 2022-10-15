package telegrambot.service;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import telegrambot.domain.Idioma;
import telegrambot.domain.Joke;
import telegrambot.domain.TipoResposta;
import telegrambot.dto.BotAnswer;
import telegrambot.i18n.ConfigLocalMessage;
import telegrambot.utils.RegexUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import static telegrambot.service.BotMessages.*;

@Getter
@Setter
@Log
public class JokeService implements ConfigLocalMessage {

    private Joke joke;
    private HttpService http;
    private boolean isReveal;
    private Idioma idioma;

    public JokeService() {
        this.http = new HttpService();
        this.isReveal = true;
        this.idioma = Idioma.PT;
    }

    public Joke getNewJoke() throws IOException {
        String response = http.get("https://jokeapi.dev/joke/Any?format=json&blacklistFlags=nsfw,racist,sexist&type=twopart&lang="
                + idioma.toString().toLowerCase(Locale.ROOT));

        joke = new Gson().fromJson(response, Joke.class);

        log.info("Joke here ---> " + joke.getSetup());
        log.info("Joke answer ---> " + joke.getDelivery());
        return joke;
    }

    public BotAnswer validateTypeOfMessage(String userMessage) {
        if(userMessage.contains("/start")){
            return BotAnswer.builder()
                    .message(BOT_LANGUAGE)
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(new String [] {"PT \uD83C\uDDE7\uD83C\uDDF7", "EN \t\uD83C\uDDFA\uD83C\uDDF8", "ES \uD83C\uDDEA\uD83C\uDDE6"})
                    .build();
        }
        // TODO - indentificar o idioma
        if(userMessage.matches(RegexUtil.getRegexMatchLanguague())) {
            try {
                this.idioma = IdiomaService.getIdiomaAndSetLocale(userMessage.substring(0, 2));

                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.greeting"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();

            } catch(Exception ex){
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.dont_understanding"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        //TODO - se ele deseja iniciar uma piada nova
        if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.positive")))
                && isReveal) {
            try {
                joke = getNewJoke();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(joke == null) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.exception.error_unexpected"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
            isReveal = false;

            return BotAnswer.builder()
                    .message(getTextMessage("bot.message.joke") + joke.getSetup())
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        //TODO - se usuário esta tentando adivinhar qual é a resposta
        if(!isReveal) {
            //TODO - se usuário desistiu de tentar
            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.quit")))) {
                isReveal = true;
                return BotAnswer.builder()
                        .message(String.format(getTextMessage("bot.message.joke.answer"), joke.getDelivery()))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();
            }

            if(userMessage.replaceAll("[^a-zA-Z0-9_ ]", "").equals(joke.getDelivery().toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9_ ]", ""))){
                isReveal = true;
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.joke.congrats"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();
            } else {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.joke.try_again"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(new String[] { getTextMessage("bot.choice.quit") })
                        .build();
            }
        }

        if(isReveal) {

            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.negative")))) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.see_you"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }

            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.later")))) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.maybe_later"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        return BotAnswer.builder()
                .message(getTextMessage("bot.message.dont_understanding"))
                .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                .build();
    }
}
