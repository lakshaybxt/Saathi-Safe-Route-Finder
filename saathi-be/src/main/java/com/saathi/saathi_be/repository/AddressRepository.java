package com.saathi.saathi_be.repository;

import com.saathi.saathi_be.domain.entity.Address;
import com.saathi.saathi_be.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
}
