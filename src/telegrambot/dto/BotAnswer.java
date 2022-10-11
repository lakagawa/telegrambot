package telegrambot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import telegrambot.domain.TipoResposta;

@Getter
@Setter
@Builder
public class BotAnswer {
    private String message;
    private String[] listaButton;
    private TipoResposta tipoMensagem;
}
