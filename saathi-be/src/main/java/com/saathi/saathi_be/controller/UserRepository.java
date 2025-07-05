package com.saathi.saathi_be.controller;

import com.saathi.saathi_be.domain.entity.User;
import org.springframework.data.repository.Repository;

import java.util.UUID;

interface UserRepository extends Repository<User, UUID> {
}
