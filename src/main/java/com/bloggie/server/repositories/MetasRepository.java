package com.bloggie.server.repositories;

import com.bloggie.server.domain.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetasRepository extends JpaRepository<Meta, Long> {
}
