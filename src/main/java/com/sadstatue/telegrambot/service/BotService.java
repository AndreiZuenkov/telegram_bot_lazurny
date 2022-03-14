package com.sadstatue.telegrambot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotService {

    private UserService userService;

    public BotService(UserService userService) {
        this.userService = userService;
    }

    public BotApiMethod<?> checkUpdate(Update update){


        if(update !=null && update.getMessage() !=null){
            switch (update.getMessage().getText()){
                case ("/start"):
                return userService.chekUser(update);
            }

        }else if(update.getCallbackQuery() !=null){
            return userService.setAdditionalInformation(update);
        }


        return null;
    }

}
