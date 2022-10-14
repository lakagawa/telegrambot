package telegrambot.service;

import telegrambot.domain.Idioma;

import java.util.Locale;

public class IdiomaService {
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
