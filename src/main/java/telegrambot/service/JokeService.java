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
        if(userMessage.contains("pt ")
                || userMessage.contains("en ")
                || userMessage.contains("es ")) {
            try {
                this.idioma = IdiomaService.getIdiomaAndSetLocale(userMessage.substring(0, 2));

                return BotAnswer.builder()
                        .message(getTextMessage("bot.greeting"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("choice.jokes").split("\\|"))
                        .build();

            } catch(Exception ex){
                return BotAnswer.builder()
                        .message(getTextMessage("bot_dont_understanding"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        //TODO - se ele deseja iniciar uma piada nova
        if(userMessage.contains("bora")
                || userMessage.contains("vamos lá")
                || userMessage.contains("manda") && isReveal) {

            try {
                joke = getNewJoke();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(joke == null) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.unexpected.error"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
            isReveal = false;

            return BotAnswer.builder()
                    .message(getTextMessage("bot.joke.message") + ": " + joke.getSetup())
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        //TODO - se usuário esta tentando adivinhar qual é a resposta
        if(!isReveal) {
            //TODO - se usuário desistiu de tentar
            if(userMessage.contains("desisto")) {
                isReveal = true;
                return BotAnswer.builder()
                        .message(String.format(getTextMessage("bot.joke.answer"), joke.getDelivery()))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("choice.jokes").split("\\|"))
                        .build();
            }

            if(userMessage.replaceAll("[^a-zA-Z0-9_ ]", "").equals(joke.getDelivery().toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9_ ]", ""))){
                isReveal = true;
                return BotAnswer.builder()
                        .message(getTextMessage("bot.joke.congrats"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("choice.jokes").split("\\|"))
                        .build();
            } else {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.joke.try_again"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(new String[] { getTextMessage("choice.quit") })  // TODO Verificar se ocorre erro pela falta do "|"
                        .build();
            }
        }

        if(isReveal && userMessage.toLowerCase().contains("nope")) {
            return BotAnswer.builder()
                    .message(getTextMessage("bot.see_you"))
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        if(isReveal && userMessage.contains("talvez mais tarde")) {
            return BotAnswer.builder()
                    .message(BOT_MAYBE_LATER)
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        return BotAnswer.builder()
                .message(getTextMessage("bot_dont_understanding"))
                .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                .build();
    }
}
