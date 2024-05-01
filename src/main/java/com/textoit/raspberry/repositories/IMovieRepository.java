package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, UUID> {

    public Movie findByTitle(String title);
}
