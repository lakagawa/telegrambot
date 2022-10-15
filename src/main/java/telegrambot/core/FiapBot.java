package telegrambot.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.java.Log;
import telegrambot.config.Initialization;
import telegrambot.dto.BotAnswer;
import telegrambot.service.BotService;
import telegrambot.service.JokeService;

import java.util.List;
import java.util.Locale;

@Log
public class FiapBot {

    /**
     * Inicializa a execução do FiapBot
     */
    public void start() {
        Initialization initialization = new Initialization();

        // Criacao do objeto bot com as informacoes de acesso.
        TelegramBot bot = new TelegramBot(initialization.getToken());

        // Objeto responsavel por receber as mensagens.
        GetUpdatesResponse updatesResponse;

        // Objeto responsavel por gerenciar o envio de respostas.
        SendResponse sendResponse;

        // Objeto responsavel por gerenciar o envio de acoes do chat.
        BaseResponse baseResponse;

        // Controle de off-set, isto e, a partir deste ID sera lido as mensagens
        // pendentes na fila.
        int offSetStart = 0;

        JokeService jokeService = new JokeService();
        BotService botService = new BotService();

        // Loop infinito pode ser alterado por algum timer de intervalo curto.
        while(true) {
            // Executa comando no Telegram para obter as mensagens pendentes a partir de um
            // off-set (limite inicial).
            updatesResponse = bot.execute(new GetUpdates().limit(initialization.getUpdatesLimit()).offset(offSetStart));

            // Lista de mensagens.
            List<Update> updates = updatesResponse.updates();

            // Analise de cada acao da mensagem.
            for(Update update : updates) {

                // Atualizacao do off-set.
                offSetStart = update.updateId() + 1;

                if(update.message() != null) {
                    log.info("Recebendo mensagem: " + update.message().text());

                    // Envio de "Escrevendo" antes de enviar a resposta.
                    baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

                    // Verificacao de acao de chat foi enviada com sucesso.
                    log.info("Resposta de Chat Action Enviada? " + baseResponse.isOk());

                    // Envio da mensagem de resposta.

                    String msgUpdated = update.message().text().toLowerCase(Locale.ROOT);

                    BotAnswer botAnswer = botService.validateTypeOfMessage(msgUpdated);

                    log.info("id? " + update.message().chat().id());

                    sendResponse = bot.execute(botService.createSendMessageObject(update.message().chat().id(), botAnswer, update.message().messageId()));

                    // Verificacao de mensagem enviada com sucesso.
                    log.info("Mensagem Enviada? " + sendResponse.isOk());
                }
            }
        }
    }

}
