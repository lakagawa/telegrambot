package telegrambot.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Reflete todas as configurações mandatórias para a inicialização do aplicativo
 * <ul>
 *  <li>Token
 *  <li>Limite de updates
 * </ul>
 */
public interface Bootable {

    String getToken();

    int getUpdatesLimit();

    /**
     * @deprecated
     * Obtém o conteúdo do arquivo
     * @param filepath Caminho completo do arquivo
     * @return O conteúdo (em texto) do arquivo informado
     * @throws IOException Em decorrência de problemas ao ler o arquivo
     */
    @Deprecated
    static String getContent(String filepath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filepath))){
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    /**
     * Parâmetros mandatórios para inicialização do aplicativo
     */
    enum Parameter {

        /**
         * @see System#getenv(String)
         */
        @Deprecated TOKEN_FILEPATH("telegramBot.token.filepath"),

        UPDATES_LIMIT("telegramBot.updates.limit");

        private String value;

        /**
         * Inicializa os parâmetros
         * @param value Parâmetro a ser inicializado
         */
        Parameter(String value) {
            this.value = value;
        }

        /**
         * Obtém a chave do parâmetro
         * @return O valor da chave
         */
        public String getValue() {
            return this.value;
        }
    }

}
