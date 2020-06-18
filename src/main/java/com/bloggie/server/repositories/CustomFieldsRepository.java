package com.bloggie.server.repositories;

import com.bloggie.server.domain.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomFieldsRepository extends JpaRepository<CustomField, Long> {
    Optional<CustomField> findByFieldName(String fieldName);
}
