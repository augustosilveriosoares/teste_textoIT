package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IMovieRepository extends JpaRepository<Movie, UUID> {

    public Movie findByTitle(String title);

    @Transactional
    @Modifying
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE movies; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    void truncateTable();
}
