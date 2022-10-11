package telegrambot.service;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import telegrambot.domain.Joke;
import telegrambot.domain.TipoResposta;
import telegrambot.dto.BotAnswer;

import java.io.IOException;
import java.util.Locale;

import static telegrambot.service.BotMessages.*;

@Getter
@Setter
public class JokeService {

    private Joke joke;
    private HttpService http;
    private boolean isReveal;

    public JokeService() {
        this.http = new HttpService();
        this.isReveal = true;
    }

    public Joke getNewJoke() throws IOException {
        String response = http.get("https://jokeapi.dev/joke/Any?format=json&blacklistFlags=nsfw,racist,sexist&lang=en&type=twopart");

        Gson gson = new Gson();
        joke = gson.fromJson(response, Joke.class);

        System.out.println("Joke here ---> " + joke.getSetup());
        System.out.println("Joke answer ---> " + joke.getDelivery());
        return joke;
    }

    public BotAnswer validateTypeOfMessage(String userMessage) {

        //TODO - se for de boas vindas
        if(userMessage.contains("boa tarde")
                || userMessage.contains("boa noite")
                || userMessage.contains("bom dia")) {
            return BotAnswer.builder()
                    .message(BOT_GREETINGS)
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(new String [] {"Bora!", "Talvez mais tarde", "Nope"})
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
                        .message(BOT_UNEXPECTED_ERROR)
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
            isReveal = false;

            return BotAnswer.builder()
                    .message(BOT_JOKE_MESSAGE_SETUP + ": " + joke.getSetup())
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        //TODO - se usuário esta tentando adivinhar qual é a resposta
        if(!isReveal) {
            //TODO - se usuário desistiu de tentar
            if(userMessage.contains("desisto")) {
                isReveal = true;
                return BotAnswer.builder()
                        .message(String.format(BOT_ANSWER, joke.getDelivery()))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }

            if(userMessage.equals(joke.getDelivery().toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9_ ]", ""))){
                isReveal = true;
                return BotAnswer.builder()
                        .message(BOT_JOKE_CONGRATS)
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            } else {
                return BotAnswer.builder()
                        .message(BOT_JOKE_TRY_AGAIN)
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        if(isReveal && userMessage.contains("nao")) {
            return BotAnswer.builder()
                    .message(BOT_SEE_YOU)
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }


        return BotAnswer.builder()
                .message(BOT_DONT_UNDERSTANDING)
                .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                .build();
    }
}
