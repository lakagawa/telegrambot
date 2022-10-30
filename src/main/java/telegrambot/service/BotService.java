package telegrambot.service;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import telegrambot.domain.TipoResposta;
import telegrambot.dto.BotAnswer;
import telegrambot.i18n.ConfigLocalMessage;
import telegrambot.utils.RegexUtil;

import java.io.IOException;
import java.util.Locale;

/**
 * BotService - Classe responsável pela lógica do BOT
 */
public class BotService implements ConfigLocalMessage {

    private JokeService jokeService;

    public BotService(){
        jokeService = new JokeService();
    }

    /**
     * Método monta o objeto SendMessage de acordo com a mensagem a ser enviada ao usuário.
     * Se a mensagem é simples ela tem resposta aberta
     * Se a mensagem tem multipla escolha o método além de receber a mensagem considera as opções
     *
     * @param chatId ID do chat
     * @param botAnswer Tipo de Resposta
     * @param messageId ID da mensagem
     * @return SendMessage
     */
    public SendMessage createSendMessageObject(Long chatId, BotAnswer botAnswer, Integer messageId) {
        SendMessage sendMessage = new SendMessage(chatId, botAnswer.getMessage());

        if(botAnswer.getTipoMensagem() == TipoResposta.SIMPLE_MESSAGE)
            //remove as opções caso adicionado em outro momento
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

    /**
     * Método responsável por identificar a resposta do usuário e montar uma resposta
     * @param userMessage Mensagem enviado pelo cliente/usuário
     * @return Objeto de resposta preenchido
     */
    public BotAnswer validateTypeOfMessage(String userMessage) {
        // primeira iteração do usuário - Responde perguntando qual o idioma do usuário (i18n)
        if(userMessage.contains("/start") || userMessage.contains("/language")){
            return BotAnswer.builder()
                    .message(getTextMessage("bot.choice.language"))
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(getTextMessage("bot.choice.language.options").split("\\|"))
                    .build();
        }

        // método padrão do telegram para ajudar o usuário identificar o que o bot pode fazer
        if(userMessage.contains("/help")){
            return BotAnswer.builder()
                    .message(getTextMessage("bot.message.help"))
                    .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                    .listaButton(new String[]{"/start"})
                    .build();
        }

        // verifica o idioma escolhido e retorna "boas vindas" - se não consegue classificar o idioma responde que nao entendeu
        if(userMessage.matches(RegexUtil.getRegexMatchLanguague())) {
            try {
                Locale.setDefault(new Locale(userMessage.substring(0, 2)));

                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.greeting"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();

            } catch(Exception ex){
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.dont_understanding"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        /* verifica se usuário quer uma piada nova
           variável isReveal é responsável por identificar se:
                * é necessário consultar uma piada nova ou
                * o fluxo ainda não começou ou
                * ainda esta na tentativa de adivinhar a resposta */
        if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.positive")))
                && jokeService.isReveal()) {
            try {
                // busca uma piada nova na API pública
                jokeService.getNewJoke();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(jokeService.getJoke() == null) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.exception.error_unexpected"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
            // reveal como false - significa que a piada esta em fase de tentar adivinhar
            jokeService.setReveal(false);

            return BotAnswer.builder()
                    .message(getTextMessage("bot.message.joke") + jokeService.getJoke().getSetup())
                    .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                    .build();
        }

        // verifica se o usuário esta tentando adivinhar a piada
        if(!jokeService.isReveal()) {

            // verifica se usuário desistiu de adivinhar
            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.quit")))) {
                jokeService.setReveal(true);
                return BotAnswer.builder()
                        .message(String.format(getTextMessage("bot.message.joke.answer"), jokeService.getJoke().getDelivery()))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();
            }

            // verificar se o usuário acertou ou errou a resposta da piada
            if(userMessage.replaceAll("[^a-zA-Z0-9_ ]", "").equals(jokeService.getJoke().getDelivery().toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z0-9_ ]", ""))){
                jokeService.setReveal(true);
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.joke.congrats"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(getTextMessage("bot.choice.jokes").split("\\|"))
                        .build();
            } else {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.joke.try_again"))
                        .tipoMensagem(TipoResposta.MULTIPLE_CHOICE_MESSAGE)
                        .listaButton(new String[] { getTextMessage("bot.choice.quit") })
                        .build();
            }
        }

        // quando ainda nao iniciou o fluxo de piada
        if(jokeService.isReveal()) {

            // verifica se usuário não quer receber piada
            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.negative")))) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.see_you"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }

            // verifica se usuário respondeu "talvez mais tarde"
            if(userMessage.matches(RegexUtil.getRegexCaseInsensitive(getTextMessage("bot.choice.later")))) {
                return BotAnswer.builder()
                        .message(getTextMessage("bot.message.maybe_later"))
                        .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                        .build();
            }
        }

        return BotAnswer.builder()
                .message(getTextMessage("bot.message.dont_understanding"))
                .tipoMensagem(TipoResposta.SIMPLE_MESSAGE)
                .build();
    }
}
