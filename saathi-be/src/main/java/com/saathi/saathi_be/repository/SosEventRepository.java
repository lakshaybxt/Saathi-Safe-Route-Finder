package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.SosEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SosEventRepository extends JpaRepository<SosEvent, UUID> {
}
