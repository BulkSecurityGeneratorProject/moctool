package com.morgan.andy.repository;

import com.morgan.andy.domain.Achievement;
import com.morgan.andy.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface HomeworkRepository extends JpaRepository<Homework, Long> {


}
