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
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE recommendations; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    void truncateTable();

}
