package telegrambot.service;

import telegrambot.domain.Idioma;

import java.util.Locale;

/**
 * IdiomaService - Classe responsável por tratar de assuntos referente a idioma
 */
public class IdiomaService {
    /**
     * Método para transformar o que foi digitado pelo usuário em um ENUM (mais fácil de utilizar dentro do código)
     * e também setar o Locale utilizado pelo i18n (carregar o idioma corretamente)
     * @param idioma
     * @return
     */
    public static Idioma getIdiomaAndSetLocale(String idioma) {
        Idioma eIdioma = Idioma.valueOf(idioma.toUpperCase(Locale.ROOT));

        switch (eIdioma){
            case EN:
                Locale.setDefault(new Locale("en", "US"));
                break;
            case ES:
                Locale.setDefault(new Locale("es", "ES"));
                break;
            case PT:
                Locale.setDefault(new Locale("pt", "BR"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + eIdioma);
        }
        return eIdioma;
    }
}
