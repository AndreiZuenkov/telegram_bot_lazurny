package com.sadstatue.telegrambot.persistence.repository;

import com.sadstatue.telegrambot.persistence.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

}
