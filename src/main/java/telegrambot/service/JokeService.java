package telegrambot.service;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import telegrambot.domain.Joke;

import java.io.IOException;
import java.util.Locale;

/**
 * Classe responsável por gerenciar piadas
 * @author Lais Kagawa
 */
@Getter
@Setter
@Log
public class JokeService {

    private Joke joke;
    private HttpService http;
    //controla o fluxo da piada
    private boolean isReveal;

    public JokeService() {
        this.http = new HttpService();
        this.isReveal = true;
    }

    /**
     * Método para consulta de uma piada em uma API pública respeitando o idioma selecionado pelo usuário
     * realiza o log de piada e da resposta no console
     * @return Uma nova piada
     * @throws IOException Na ocorrência de falha ao acessar/ler a API
     */
    public Joke getNewJoke() throws IOException {
        String response = http.get("https://jokeapi.dev/joke/Any?format=json&blacklistFlags=nsfw,racist,sexist&type=twopart&lang="
                + Locale.getDefault().getLanguage());

        joke = new Gson().fromJson(response, Joke.class);

        log.info("Joke here ---> " + joke.getSetup());
        log.info("Joke answer ---> " + joke.getDelivery());
        return joke;
    }

}
