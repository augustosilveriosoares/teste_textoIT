package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.MovieList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface IMovieListRepository extends JpaRepository<MovieList, UUID> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE movielist", nativeQuery = true)
    void truncateTable();
}
