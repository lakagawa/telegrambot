package telegrambot.config;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * <P>
 * Inicializa o arquivo de configurações base da aplicação
 */
public class Initialization implements Bootable {

    /**
     * Arquivo de configuração carregado
     */
    ResourceBundle config = ResourceBundle.getBundle("config");

    /**
     * Carrega o token presente no arquivo
     * @return Token registrado no arquivo
     */
    @Override
    public String getToken() {
        /*
        try {
            return Bootable.getContent(config.getString(Parameter.TOKEN_FILEPATH.getValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        return System.getenv("TOKEN_BOT");
    }

    /**
     * Carrega o limite de atualizações do arquivo de configuraçõa da aplicação
     * @return Limite de atualizações parametrizado
     */
    @Override
    public int getUpdatesLimit() {
        return Integer.parseInt(config.getString(Parameter.UPDATES_LIMIT.getValue()));
    }

}
