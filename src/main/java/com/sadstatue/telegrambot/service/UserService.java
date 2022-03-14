package com.sadstatue.telegrambot.service;

import com.sadstatue.telegrambot.persistence.model.User;
import com.sadstatue.telegrambot.persistence.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepo userRepo;

    private final String message = "Вы уже зарегистрированы";


    private String greetingMessage = "Добро пожаловать!";


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public SendMessage chekUser(Update update) {

        User userFromDb = userRepo.findByChatId(update.getMessage().getChatId());

        if (userFromDb != null) {
            if (userFromDb.getHouse() == 0) {
                return addHouseNumber(update);
            }
            return new SendMessage(Long.toString(update.getMessage().getChatId()), message);
        }

        createNewUser(update);

        return new SendMessage(Long.toString(update.getMessage().getChatId()), greetingMessage);

    }

    private void createNewUser(Update update) {

        User user = new User();
        user.setChatId(update.getMessage().getChatId());
        user.setFirstName(update.getMessage().getFrom().getFirstName());
        user.setRegistrationDate(LocalDate.now());
        userRepo.save(user);

    }

    private SendMessage addHouseNumber(Update update) {

        SendMessage message = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonIzumrud1 = new InlineKeyboardButton();
        buttonIzumrud1.setText("Изумрудная 1");
        buttonIzumrud1.setCallbackData("Izumrud");

        InlineKeyboardButton buttonPribrez1 = new InlineKeyboardButton();
        buttonPribrez1.setText("Прибрежный бульвар 1");
        buttonPribrez1.setCallbackData("Pribrez1");

        InlineKeyboardButton buttonPribrez3 = new InlineKeyboardButton();
        buttonPribrez3.setText("Прибрежный бульвар 3");
        buttonPribrez3.setCallbackData("Pribrez3");

        List<InlineKeyboardButton> keyboardButtonList1 = new ArrayList<>() {{
            add(buttonIzumrud1);
        }};
        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>() {{
            add(buttonPribrez1);
        }};
        List<InlineKeyboardButton> keyboardButtonList3 = new ArrayList<>() {{
            add(buttonPribrez3);
        }};
        List<List<InlineKeyboardButton>> listKeyBoard = new ArrayList<>() {{
            add(keyboardButtonList1);
            add(keyboardButtonList2);
            add(keyboardButtonList3);
        }};

        inlineKeyboardMarkup.setKeyboard(listKeyBoard);


        message.setChatId(Long.toString(update.getMessage().getChatId()));
        message.setText("Выберете дом в котором проживаете");
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    public SendMessage setAdditionalInformation(Update update) {

        User userFromDb = userRepo.findByChatId(update.getCallbackQuery().getMessage().getChatId());

        if (userFromDb.getHouse() == 0) {
            switch (update.getCallbackQuery().getData()) {
                case "Izumrud":
                    userFromDb.setHouse(1);
                    break;
                case "Pribrez1":
                    userFromDb.setHouse(2);
                    break;
                case "Pribrez3":
                    userFromDb.setHouse(3);
                    break;
            }
        }

        userRepo.save(userFromDb);

        return addApartmentNumber(update);
    }

    private SendMessage addApartmentNumber(Update update) {

        SendMessage message=new SendMessage();

        message.setChatId(Long.toString(update.getCallbackQuery().getMessage().getChatId()));
        message.setText("Введите номер вашей квартиры");

        return  message;
    }
}
