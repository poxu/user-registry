package com.company.registry.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    //я бы оставил id
    @Column(name = "user_id")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    //все поля нужно сделать private
    private UUID id;
    String name;
    String surname;
    String patronymic;
    int age; // birthdate? age это возраст регистрациИ? а если актуальный возраст, то нужно каждый год обновлять
    Occupation occupation;
    String passport;
}
