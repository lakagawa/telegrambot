package telegrambot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import telegrambot.domain.Joke;
import telegrambot.dto.BotAnswer;
import telegrambot.service.JokeService;

public class Main {

	public static void main(String[] args) {
		// Criacao do objeto bot com as informacoes de acesso.
		TelegramBot bot = new TelegramBot(System.getenv("TOKEN_BOT"));

		// Objeto responsavel por receber as mensagens.
		GetUpdatesResponse updatesResponse;

		// Objeto responsavel por gerenciar o envio de respostas.
		SendResponse sendResponse;

		// Objeto responsavel por gerenciar o envio de acoes do chat.
		BaseResponse baseResponse;

		// Controle de off-set, isto e, a partir deste ID sera lido as mensagens
		// pendentes na fila.
		int m = 0;
		boolean fisrt = true;

		String msg = "";
		Joke joke = null;
		JokeService ps = new JokeService();

		// Loop infinito pode ser alterado por algum timer de intervalo curto.
		while (true) {
			// Executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial).
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// Lista de mensagens.
			List<Update> updates = updatesResponse.updates();

			// Analise de cada acao da mensagem.
			for (Update update : updates) {

				// Atualizacao do off-set.
				m = update.updateId() + 1;

				if(update.message() != null) {
					System.out.println("Recebendo mensagem: " + update.message().text());

					// Envio de "Escrevendo" antes de enviar a resposta.
					baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

					// Verificacao de acao de chat foi enviada com sucesso.
					System.out.println("Resposta de Chat Action Enviada? " + baseResponse.isOk());

					// Envio da mensagem de resposta.

					String msgUpdated = update.message().text().toLowerCase(Locale.ROOT);

					BotAnswer botAnswer = ps.validateTypeOfMessage(msgUpdated);

					System.out.println("id? " + update.message().chat().id());



//					String[] lista = new String [] {"Bora!", "Talvez mais tarde", "Nope"};
//
//					ReplyKeyboardMarkup replyKeyboardMarkup = null;
//					replyKeyboardMarkup = new ReplyKeyboardMarkup(lista);
//					replyKeyboardMarkup.oneTimeKeyboard(true);
//
//
//					SendMessage mess = new SendMessage(update.message().chat().id(), msgFinal);
//					mess.replyMarkup(replyKeyboardMarkup);
//					mess.replyToMessageId(update.message().messageId());

//
//					replyKeyboardMarkup = new ReplyKeyboardMarkup(new String[]{});
//					mess.replyMarkup(replyKeyboardMarkup);
//
//
//					//funciona
//					sendResponse = bot.execute(mess);

					//montar aqui antes de mandar a resposta :D

					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), botAnswer.getMessage()));


					//new ReplyKeyboardMarkup(lista, true, true, "", true);
					// Verificacao de mensagem enviada com sucesso.
					System.out.println("Mensagem Enviada? " + sendResponse.isOk());
					fisrt = false;
				}
			}
		}
	}
}
