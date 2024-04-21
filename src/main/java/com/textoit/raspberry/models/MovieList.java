package com.textoit.raspberry.models;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.rmi.server.UID;
import java.util.UUID;


@Entity
@Table(name="movielist")
@NoArgsConstructor
@Getter
@Setter



public class MovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="ano")
    private Long year;
    private String title;
    private String studios;
    private String producers;
    private Boolean winner;



}
