package telegrambot.service;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import telegrambot.domain.TipoResposta;
import telegrambot.dto.BotAnswer;

import java.util.List;

public class BotService {

    public SendMessage createSendMessageObject(Long chatId, BotAnswer botAnswer, Integer messageId) {
        SendMessage sendMessage = new SendMessage(chatId, botAnswer.getMessage());

        if(botAnswer.getTipoMensagem() == TipoResposta.SIMPLE_MESSAGE)
            sendMessage.replyMarkup(new ReplyKeyboardRemove());
        else {
            ReplyKeyboardMarkup replyKeyboardMarkup = null;
            replyKeyboardMarkup = new ReplyKeyboardMarkup(botAnswer.getListaButton());
            replyKeyboardMarkup.oneTimeKeyboard(true);

            sendMessage.replyMarkup(replyKeyboardMarkup);
            sendMessage.replyToMessageId(messageId);
        }

        return sendMessage;
    }
}
