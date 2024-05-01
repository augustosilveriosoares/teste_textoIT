package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Recommendation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IRecommendationRepository extends JpaRepository<Recommendation, UUID> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE recommendations", nativeQuery = true)
    void truncateTable();
}
