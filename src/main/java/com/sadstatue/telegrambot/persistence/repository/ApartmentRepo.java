package com.sadstatue.telegrambot.persistence.repository;

import com.sadstatue.telegrambot.persistence.model.Apartment;
import org.springframework.data.repository.CrudRepository;

public interface ApartmentRepo extends CrudRepository<Apartment, Long> {
}
