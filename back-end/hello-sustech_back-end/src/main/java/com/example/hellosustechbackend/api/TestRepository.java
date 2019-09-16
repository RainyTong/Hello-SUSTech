package com.example.hellosustechbackend.api;

import com.example.hellosustechbackend.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    Test findById(int id);
}
