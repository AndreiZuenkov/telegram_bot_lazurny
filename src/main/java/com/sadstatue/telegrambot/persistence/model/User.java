package com.sadstatue.telegrambot.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "chat_id")
    private long chatId;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "first_name")
    private String firstName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

}
