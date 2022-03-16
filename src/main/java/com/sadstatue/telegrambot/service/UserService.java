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
    private long chatIdFromUpdate;

    private final String message = "Вы уже зарегистрированы";


    private String greetingMessage = "Добро пожаловать!";


    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public SendMessage chekUser1(Update update) {

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


    public SendMessage chekUser(Update update) {

        User userFromDb = null;

        if (update.getMessage() != null) {
            chatIdFromUpdate = update.getMessage().getChatId();
            userFromDb = userRepo.findByChatId(chatIdFromUpdate);
        } else if (update.getCallbackQuery() != null) {
            chatIdFromUpdate = update.getCallbackQuery().getMessage().getChatId();
            userFromDb = userRepo.findByChatId(update.getCallbackQuery().getMessage().getChatId());
        }

        if (userFromDb == null) {

            return createNewUser(update);

        } else return chekMessage(update);

    }

    private SendMessage chekMessage(Update update) {

        if (update.getMessage() != null) {
            return chekMessageAnswer(update);
        } else if (update.getCallbackQuery() != null) {
            return chekCallbackQueryAnswer(update);
        }

        return new SendMessage(Long.toString(chatIdFromUpdate), "Что то пошло не так!");
    }

    private SendMessage chekCallbackQueryAnswer(Update update) {

        if (userRepo.findByChatId(chatIdFromUpdate).getHouse() == 0) {
            return setHouseNumber(update);
        }


        return new SendMessage(Long.toString(chatIdFromUpdate), "Что то пошло не так!");
    }

    private SendMessage chekMessageAnswer(Update update) {

        if(userRepo.findByChatId(chatIdFromUpdate).getApartmentNumber() == 0){
            return setApartmentNumber(update);
        }

        switch (update.getMessage().getText()){
            case "/hello":
                return new SendMessage(Long.toString(chatIdFromUpdate), "Hello user!");

        }

        return null;
    }

    private SendMessage chekCallbackQuery(Update update) {

        User userFromDb = userRepo.findByChatId(chatIdFromUpdate);

        if (userFromDb != null && userFromDb.getHouse() == 0) {
            setHouseNumber(update);
            return addApartmentNumber(update);
        } else if (userFromDb != null && userFromDb.getApartmentNumber() == 0) {
            setApartmentNumber(update);
            return new SendMessage(Long.toString(chatIdFromUpdate), "Дальше выскакивает нижнее меню и погнали");
        }

        return null;
    }


    private SendMessage createNewUser(Update update) {

        User user = new User();
        user.setChatId(update.getMessage().getChatId());
        user.setFirstName(update.getMessage().getFrom().getFirstName());
        user.setRegistrationDate(LocalDate.now());
        userRepo.save(user);

        return addHouseNumber(update);
    }

    private SendMessage addHouseNumber(Update update) {

        SendMessage message = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonIzumrud1 = new InlineKeyboardButton();
        buttonIzumrud1.setText("Изумрудная 1");
        buttonIzumrud1.setCallbackData("Iz");

        InlineKeyboardButton buttonPribrez1 = new InlineKeyboardButton();
        buttonPribrez1.setText("Прибрежный бульвар 1");
        buttonPribrez1.setCallbackData("Pr1");

        InlineKeyboardButton buttonPribrez3 = new InlineKeyboardButton();
        buttonPribrez3.setText("Прибрежный бульвар 3");
        buttonPribrez3.setCallbackData("Pr3");

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


        message.setChatId(Long.toString(chatIdFromUpdate));
        message.setText("Выберете дом в котором проживаете");
        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    public SendMessage setHouseNumber(Update update) {

        User userFromDb = userRepo.findByChatId(chatIdFromUpdate);

        if (userFromDb.getHouse() == 0) {
            switch (update.getCallbackQuery().getData()) {
                case "Iz":
                    userFromDb.setHouse(1);
                    break;
                case "Pr1":
                    userFromDb.setHouse(2);
                    break;
                case "Pr3":
                    userFromDb.setHouse(3);
                    break;
                default:
                    addHouseNumber(update);
            }
        }

        userRepo.save(userFromDb);

        return addApartmentNumber(update);
    }

    private SendMessage addApartmentNumber(Update update) {

        SendMessage message = new SendMessage();

//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//
//        InlineKeyboardButton[] inlineKeyboardButtons = new InlineKeyboardButton[40];
//
//        for (int i = 1; i <= 40; i++) {
//            inlineKeyboardButtons[i] = new InlineKeyboardButton();
//            inlineKeyboardButtons[i].setText(Integer.toString(i));
//            inlineKeyboardButtons[i].setCallbackData(Integer.toString(i));
//
//        }
//
//        List<InlineKeyboardButton> keyboardButtonList = new ArrayList<>();
//
//        for (int i = 0; i < 40; i++) {
//            keyboardButtonList.add(inlineKeyboardButtons[i]);
//        }
//        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        list.add(keyboardButtonList);
//
//        inlineKeyboardMarkup.setKeyboard(list);

        message.setChatId(Long.toString(chatIdFromUpdate));
        message.setText("Введите номер вашей квартиры");
//        message.setReplyMarkup(inlineKeyboardMarkup);

        return message;
    }

    public SendMessage setApartmentNumber(Update update) {

        SendMessage message = new SendMessage();

        message.setChatId(Long.toString(chatIdFromUpdate));

        User userFromDb = userRepo.findByChatId(chatIdFromUpdate);


        if (update.getMessage().getText().matches("\\d+")) {

            userFromDb.setApartmentNumber(Integer.parseInt(update.getMessage().getText()));
        } else {

            return addApartmentNumber(update);
        }

        userRepo.save(userFromDb);

        message.setText("Вы успешно заполнили всю необходимую информацию");

        return message;
    }

    private SendMessage chekAdditionParameters(Update update) {

        User userFromDb = userRepo.findByChatId(chatIdFromUpdate);

        if (userFromDb.getHouse() == 0 || userFromDb.getApartmentNumber() == 0) {
            if (userFromDb.getHouse() == 0) {
                if (update.getMessage() != null) {
                    setHouseNumber(update);
                }
                addHouseNumber(update);
            } else setApartmentNumber(update);
        }


        return null;
    }
}
