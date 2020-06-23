package com.bloggie.server.repositories;

import com.bloggie.server.domain.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomFieldsRepository extends JpaRepository<CustomField, Long> {
    Optional<CustomField> findByFieldName(String fieldName);
    Boolean existsByFieldName(String fieldName);
    List<CustomField> findAllByValue(String value);
}
