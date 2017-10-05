package main.bot;

import main.JokeBot;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.*;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.updateshandlers.SentCallback;

import java.io.Serializable;

public class MessageSender extends AbsSender {

        @Override
        public Message sendDocument(SendDocument sendDocument) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendPhoto(SendPhoto sendPhoto) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendVideo(SendVideo sendVideo) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendVideoNote(SendVideoNote sendVideoNote) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendSticker(SendSticker sendSticker) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendAudio(SendAudio sendAudio) throws TelegramApiException {
            return null;
        }

        @Override
        public Message sendVoice(SendVoice sendVoice) throws TelegramApiException {
            return null;
        }

        @Override
        protected <T extends Serializable, Method extends BotApiMethod<T>, Callback extends SentCallback<T>> void sendApiMethodAsync(Method method, Callback callback) {

        }

        @Override
        protected <T extends Serializable, Method extends BotApiMethod<T>> T sendApiMethod(Method method) throws TelegramApiException {
            return null;
        }

    protected void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    protected void sendMsg(Message message, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId().toString());
        sendMsg.setReplyToMessageId(message.getMessageId());
        sendMsg.setText(text);
        try {
            sendMessage(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
