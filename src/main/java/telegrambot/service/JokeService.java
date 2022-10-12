package telegrambot.service;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import telegrambot.domain.Joke;
import telegrambot.domain.TipoResposta;
import telegrambot.dto.BotAnswer;
import telegrambot.i18n.ConfigLocalMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

@Getter
@Setter
@Log
public class JokeService implements ConfigLocalMessage {

    private Joke joke;
    private HttpService http;
    private boolean isReveal;

    public JokeService() {
        this.http = new HttpService();
        this.isReveal = true;
    }

    public Joke getNewJoke() throws IOException {
        //String language = Locale.getDefault().getLanguage();    // TODO Tornar a linguagem dinamica na chamada da JokeApi
        String response = http.get("https://jokeapi.dev/joke/Any?format=json&blacklistFlags=nsfw,racist,sexist&lang=en&type=twopart");

        joke = new Gson().fromJson(response, Joke.class);

        log.info("Joke here ---> " + joke.getSetup());
        log.info("Joke answer ---> " + joke.getDelivery());
        return joke;
    }

    public BotAnswer validateTypeOfMessage(String userMessage) {

        //TODO - se for de boas vindas
        if(userMessage.contains(getTextMessage("bot.greeting.afternoon"))
                || userMessage.contains(getTextMessage("bot.greeting.night"))
                || userMessage.contains(getTextMessage("bot.greeting.morning"))) {
            return BotAnswer.builder()
                    .message(getTextMessage("bot.greeting.language"))
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(getTextMessage("choice.language").split("|"))
                    .build();
        }

        //TODO - Iniciando a brincadeira
        if(Arrays.asList(getTextMessage("choice.language").split("|")).contains(userMessage)) {
            //Locale.setDefault(new Locale("en", "US"));
            return BotAnswer.builder()
                    .message(getTextMessage("bot.greeting"))
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(getTextMessage("choice.jokes").split("|"))
                    .build();
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
                        .listaButton(getTextMessage("choice.jokes").split("|"))
                        .build();
            }

            if(userMessage.replaceAll("[^a-zA-Z0-9_ ]", "").equals(joke.getDelivery().toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9_ ]", ""))){
                isReveal = true;
                return BotAnswer.builder()
                        .message(getTextMessage("bot.joke.congrats"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("choice.jokes").split("|"))
                        .build();
            } else {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.joke.try_again"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("choice.quit").split("|"))  // TODO Verificar se ocorre erro pela falta do "|"
                        .build();
            }
        }

        if(isReveal && userMessage.toLowerCase().contains("nope")) {
            return BotAnswer.builder()
                    .message(getTextMessage("bot.see_you"))
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }


        return BotAnswer.builder()
                .message(getTextMessage("bot_dont_understanding"))
                .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                .build();
    }
}
