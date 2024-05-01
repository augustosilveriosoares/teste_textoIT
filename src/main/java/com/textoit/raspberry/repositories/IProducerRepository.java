package com.textoit.raspberry.repositories;

import com.textoit.raspberry.models.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProducerRepository extends JpaRepository<Producer, UUID> {

    public Producer findByName(String name);
}
