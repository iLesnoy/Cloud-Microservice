package com.petrovskiy.epm.telbot.service;

import com.petrovskiy.epm.telbot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class BotService extends TelegramLongPollingBot {

    @Autowired
    private BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            long chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();

            switch (message){
                case "/start":
                    startCommand(chatId,update.getMessage().getChat().getUserName());
                    break;
                default:sendMessage(chatId,"Command not supported");
            }
        }

    }

    private void startCommand(long chatId,String name){
        String answer = "hello "+ name;
        sendMessage(chatId,answer);
    }

    private void sendMessage(long chatId, String responseTest){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(responseTest);

        try {
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }
}
