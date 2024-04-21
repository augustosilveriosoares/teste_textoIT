package com.textoit.raspberry.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@Getter
@Setter
public class Award {

    private String producer;
    private Long interval;
    private Long previousWin;
    private Long followingWin;

}
