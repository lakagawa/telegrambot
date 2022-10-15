package telegrambot.service;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import telegrambot.domain.Idioma;
import telegrambot.domain.Joke;
import telegrambot.i18n.ConfigLocalMessage;

import java.io.IOException;
import java.util.Locale;

/**
 * Classe responsável por gerenciar piadas
 */
@Getter
@Setter
@Log
public class JokeService implements ConfigLocalMessage {

    private Joke joke;
    private HttpService http;
    //controla o fluxo da piada
    private boolean isReveal;
    private Idioma idioma;

    public JokeService() {
        this.http = new HttpService();
        this.isReveal = true;
        this.idioma = Idioma.PT;
    }

    /**
     * Método para consulta de uma piada em uma API pública respeitando o idioma selecionado pelo usuário
     * realiza o log de piada e da resposta no console
     * @return
     * @throws IOException
     */
    public Joke getNewJoke() throws IOException {
        String response = http.get("https://jokeapi.dev/joke/Any?format=json&blacklistFlags=nsfw,racist,sexist&type=twopart&lang="
                + idioma.toString().toLowerCase(Locale.ROOT));

        joke = new Gson().fromJson(response, Joke.class);

        log.info("Joke here ---> " + joke.getSetup());
        log.info("Joke answer ---> " + joke.getDelivery());
        return joke;
    }
}
