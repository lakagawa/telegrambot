package telegrambot.dto;

import lombok.Builder;
import lombok.Data;

import telegrambot.domain.TipoResposta;

@Data
@Builder
public class BotAnswer {
    private String message;
    private String[] listaButton;
    private TipoResposta tipoMensagem;
}
