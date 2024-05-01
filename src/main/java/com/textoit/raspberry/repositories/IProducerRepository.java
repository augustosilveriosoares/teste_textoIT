package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Producer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProducerRepository extends JpaRepository<Producer, UUID> {

    public Producer findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE producers; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    void truncateTable();
}
